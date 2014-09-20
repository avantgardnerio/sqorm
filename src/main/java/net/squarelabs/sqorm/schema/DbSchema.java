package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.annotation.Association;
import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DbSchema {

    private final DbDriver driver;

    private final Map<Class<?>, TableSchema> tables;
    private final Map<String, RelationSchema> relationships;

    public DbSchema(DbDriver driver, String namespace) {
        this.driver = driver;

        tables = getTables(driver, namespace);
        Map<String,AssociationCache> associations = getAssociations(tables);
        relationships = getRelationships(associations);
    }

    private static Map<Class<?>, TableSchema> getTables(DbDriver driver, String namespace) {
        Map<Class<?>, TableSchema> tables = new ConcurrentHashMap<>();
        Set<Class<?>> pojos = new Reflections(namespace).getTypesAnnotatedWith(Table.class);
        for(Class<?> clazz : pojos) {
            TableSchema table = new TableSchema(clazz, driver);
            tables.put(clazz, table);
        }
        return tables;
    }

    private static Map<String,AssociationCache> getAssociations(Map<Class<?>, TableSchema> tables) {
        Map<String,AssociationCache> associations = new HashMap<>();
        for(Map.Entry<Class<?>, TableSchema> entry : tables.entrySet()) {
            Class<?> clazz = entry.getKey();
            TableSchema table = entry.getValue();
            for(Method method : clazz.getMethods()) {
                Association association = method.getAnnotation(Association.class);
                if(association == null) {
                    continue;
                }
                String relName = association.name();
                AssociationCache ac = getAssociation(associations, relName);
                ac.association = association;
                if(association.isForeignKey()) {
                    ac.childTable = table;
                    if(method.getReturnType() == void.class) {
                        ac.parentSettter = method;
                    } else {
                        ac.parentGettter = method;
                    }
                } else {
                    ac.parentTable = table;
                    if(method.getReturnType() == void.class) {
                        ac.childrenSettter = method;
                    } else {
                        ac.childrenGettter = method;
                    }
                }
            }
        }
        return associations;
    }

    private static Map<String, RelationSchema> getRelationships(Map<String,AssociationCache> associations) {
        Map<String, RelationSchema> relationships = new ConcurrentHashMap<>();
        for(Map.Entry<String,AssociationCache> entry : associations.entrySet()) {
            String relName = entry.getKey();
            AssociationCache ac = entry.getValue();
            List<ColumnSchema> primaryCols = ac.parentTable.getColumns(ac.association.primaryKey().split(","));
            IndexSchema primaryIdx = ac.parentTable.ensureIndex(primaryCols);
            List<ColumnSchema> foreignCols = ac.childTable.getColumns(ac.association.foreignKey().split(","));
            IndexSchema foreignIdx = ac.childTable.ensureIndex(foreignCols);
            RelationSchema rel = new RelationSchema(relName, primaryIdx, foreignIdx);
            relationships.put(relName, rel);
        }

        return relationships;
    }

    public TableSchema getTable(Class<?> clazz) {
        return tables.get(clazz);
    }

    private static AssociationCache getAssociation(Map<String,AssociationCache> map, String name) {
        if(!map.containsKey(name)) {
            map.put(name, new AssociationCache(name));
        }
        return map.get(name);
    }

    /**
     * Temporary holder class so we can collect this info and construct a RelationSchema all at once
     */
    private static class AssociationCache {
        public String name;
        public Association association;
        public Method parentGettter;
        public Method parentSettter;
        public Method childrenGettter;
        public Method childrenSettter;
        public TableSchema parentTable;
        public TableSchema childTable;

        public AssociationCache(String name) {
            this.name = name;
        }
    }
}
