sqorm
=====

/skwôrm/ - Static Query ORM Library in Java

Features:

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


Done
----
* Simple loading and persistence test
* Initial stab at DataSet API 
* Initial Persistor implementation
* Optimistic concurrency version field
* Basic updates working
* Static query parsing and translation

In Progress
-----------
* Named parameter support

TODO
----
* Database specific resource file overrides
* Persistence ordering and/or deferred constraints
* Dirty record tracking
* Postgres, Oracle, MS-SQL
* POJO codegen
* Fluent DataSet query language
* Stored procedure support
* Blob API
* In-memory hash-map indexing
* Ordered indexing
* Mapping relationships on load
* Example Java + JS app to load graph and serialize to JSON
* JMX metrics
* Swing data binding descriptors: List, Tree, Grid
