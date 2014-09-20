sqorm
=====

**/skwôrm/** - Static Query ORM Library in Java

**Real SQL, Full ORM, DBA Friendly, never lazy loaded**.

Features
--------
* Write your own select queries in real SQL
* Results are loaded into standard POJOs
* Full graphs of POJOs are supported with .getChildren() and .getParent() accessors
* Insert / update / delete queries are generated automatically
* Order of operations for modifications are resolved automatically
* Most hand-written queries are automatically translated to the syntax of the current DB
* When complex queries are needed, DB-specific versions can be written and will be selected automatically by DB type
* The application developer controls explicitly which queries run and when
* Database performance will never suffer from lots of auto-generated queries behind the scenes
* Lifecycle of objects is explicitly controlled by the application
* Support for: PostGreSQL, Oracle, MySQL, and MS-SQL
* POJOs can be generated automatically from your database schema
* No "leaky" abstractions (1:1 POJO mapping, no library-level caching)
* Works in parallel with standard JDBC, you can keep your own connection pool and run your own statements
* Named parameter support
* Abstraction between databases
* Support for advanced concepts like database-side pagination
* Fluent query language for objects in memory
* Support for Java 8 Streams APIs
* Cached reflection for optimum performance


Design Trade-offs
-----------------
* No lazy loading - if not explicitly loaded in SQL, children won't appear in child iterators
* No impedance mapping - 1:1 POJO to table, 1:1 column to accessor
* Caching is at application level - Loads are always done by queries, queries will always access the real DB
* New library - small user base (though the concepts have been refined over 5 years)


Done
----
* Simple loading and persistence test
* Initial stab at DataSet API 
* Initial Persistor implementation
* Optimistic concurrency version field
* Basic updates working
* Static query parsing and translation
* Named parameter support
* Database specific resource file overrides
* In-memory hash-map indexing
* Mapping relationships on load

In Progress
-----------
* POJO codegen
* Fluent DataSet query language

TODO
----
* Postgres, Oracle, MS-SQL
* Persistence ordering and/or deferred constraints
* Dirty record tracking
* Stored procedure support
* Blob API
* Ordered indexing
* Example Java + JS app to load graph and serialize to JSON
* JMX metrics
* Swing data binding descriptors: List, Tree, Grid
