[![Build Status](https://travis-ci.org/dynamicloud/dynamicloud_java_api.svg?branch=master)](https://travis-ci.org/dynamicloud/dynamicloud_java_api)

# Dynamicloud Java ![alt text](https://img.shields.io/badge/api%20version-1.0.5-brightgreen.svg "")
This Java API  helps you to use the power of Dynamicloud.  This API follows our Rest documentation to execute CRUD operations according to http methods.

# Requirements

Java JDK 7 or later, you can download it on [Java Oracle site](http://www.oracle.com/technetwork/java/javase/downloads/index.html "Download Java") and Maven commands

# Installation
This mvn command will generate the Dynamicloud jar file

```bash
mvn package
```

If you want to add to your Maven project, just add this dependency:
```xml
<dependency>
  <groupId>org.dynamicloud.api</groupId>
  <artifactId>dynamicloud</artifactId>
  <version>1.0.5</version>
</dependency>
```



# Javadoc

To read the Java API documentation click [here](http://www.dynamicloud.org/javadocs/index.html "Dynamicloud Java API documentation")

# Getting started

This API provides components to execute operations on [Dynamicloud](http://www.dynamicloud.org/ "Dynamicloud") servers.  The main components and methods are the followings:

1. [RecordModel](#recordmodel)
2. [RecordCredential](#recordcredential)
3. [BoundInstance](#boundinstance)
4. [@Bind](#annotation-bind)
5. [DynamicProvider](#dynamicprovider)
  1. [DynamicProvider's methods](#methods)
6. [Query](#query-class)
  1. [RecordResults](#recordresults)
  - [Condition](#conditions-class)
  - [Conditions](#conditions-class)
  - [Between condition](#between-condition)
  - [Exists condition](#exists-condition)
  - [Join clause](#join-clause)
  - [Next, Offset and Count methods](#next-offset-and-count-methods)
  - [Order by](#order-by)
  - [Group by and Projection](#group-by-and-projection)
  - [Functions as a Projection](#functions-as-a-projection)
7. [Update using selection](#update-using-selection)
8. [Delete using selection](#delete-using-selection)

These components will allow you to connect on Dynamicloud servers, authenticate and execute operations like *loadRecord*, *updateRecord*, *deleteRecord*, *get record's information according to selection*, *get record's information according to projection*, etc.  The next step is explain every components and how to execute operations.  

# RecordModel
This class represents a model in Dynamicloud.  Every method in this API needs a model to identify a specific set of records and execute operations on them.

To instantiate a model you have to pass an ID from your models in Dynamicloud
```java
RecordModel recordModel = new RecordModel(modelId);
```

# RecordCredential
This class encapsulates the API keys to gain access on Dynamicloud servers.
```java
RecordCredential recordCredential = new RecordCredential(CSK, ACI);
```

# BoundInstance
This interface declares the required methods for a bound instance
```java
public interface BoundInstance
```

A bound instance must implement BoundInstance interface to be eligible to use in DynamicProvider methods.
```java
public class ModelField implements BoundInstance
```

# Annotation @Bind
This is an annotation to Bind local attributes and fields in Dynamicloud.
Every set method must be annotated as a Bind method:
```java
public class ModelField implements BoundInstance {
  private String email;
  
  @Bind(field = "email")
  public void setEmail(String email) {
    this.email = email;
  }
}
```

# DynamicProvider
`DynamicProvider` provides important methods and can be used as follow:
```java
public class DynamicProviderImpl<T> implements DynamicProvider<T>
```
 
**First, let's explain the constructor of this class:**
 ```java
public DynamicProviderImpl(RecordCredential credential)
 ```
This constructor receives an object with the credential to gain access.  The credential object is composed by Client Secret Key (CSK) and Application Client ID (ACI), these keys were provided at moment of your registration.
 
# Methods
 
 **Load Record**
```java
public T loadRecord(Long rid, RecordModel model, Class boundClass)
```
This method loads a record according to rid *(RecordID)* in model *(RecordModel)* the third parameter is the bound class that will contain the data of this record.  The generic type *'T'* must be the type related to boundClass parameter.

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
 
ModelField instance = provider.loadRecord(2l, model, ModelField.class);
```

**Save Record**
 ```java
public void saveRecord(RecordModel model, T instance)
```
This method saves a record (instance) that implements BoundInstance interface and must be the same type from provider's generic type..

**For example, a call of this method would be:**
 ```java

ModelField instance = new ModelField();
instance.setEmail("email@dynamicloud.org");
instance.setName("Eleazar");
instance.setLastName("Gómez");
/**
 * You can set Date, Timestamp or sql.Date object
 */
instance.setBirthdate(new Date());

DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);

provider.saveRecord(model, instance);
```

**Update Record**
 ```java
public void updateRecord(RecordModel model, T instance)
```
This method updates the record (instance) that implements BoundInstance interface and must be the same type from provider's generic type..

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
 
ModelField instance = provider.loadRecord(2L, model, ModelField.class);
instance.setEmail("email@dynamicloud.org");
 
provider.updateRecord(model, instance);
```

**Delete Record**
 ```java
public void deleteRecord(RecordModel theModel, Long rid)
```
This method deletes a record from theModel

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
 
provider.deleteRecord(model, 2L);
```

**Create query**
 ```java
public Query createQuery(RecordModel recordModel)
```
This method returns a Query to get records according specific conditions.

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
 
Query query = provider.createQuery(model);

```

# Query class

This class provides a set of methods to add conditions, order by and group by clauses, projections, etc.

```java
public Query add(Condition condition);
public Query setAlias(String alias);
public Query join(Condition condition);
public Query asc();
public Query desc();
public Query setCount(int count);
public Query setOffset(int offset);
public Query setProjection(String projection);
public Query setProjection(String[] projection);
public Query orderBy(String attribute);
public Query groupBy(String[] attributes);
public RecordResults<T> list() throws DynamicloudProviderException;
public RecordResults<T> next() throws DynamicloudProviderException;

```

With the Query object we can add conditions like EQUALS, IN, OR, AND, GREATER THAN, LESSER THAN, etc.  The query object is mutable and every call of its methods will return the same instance.

# RecordResults

**This class provides three methods:**
- `getTotalRecords` The total records in RecordModel
- `getFastReturnedSize` The returned size of records that have matched with Query conditions
- `getRecords` A list of records, the objects in this list will be `BoundInstances` according to Query's generic type.

**The uses of this class would be as a follow:**

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
/*
 This setBoundClass indicates what kind of object will be used at moment 
 of records building.  If this set it's never called getRecords method will 
 return a generic list of objects.
*/
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}
```

# Conditions class

This class provides a set of methods to build conditions and add them to query object
```java
public static Condition and(Condition left, Condition right);
public static Condition or(Condition left, Condition right);
public static Condition in(String left, String[] values);
public static Condition in(String left, Number[] values);
public static Condition in(String left, Character[] values);
public static Condition notIn(String left, String[] values);
public static Condition notIn(String left, Number[] values);
public static Condition notIn(String left, Character[] values);
public static Condition like(String left, String like);
public static Condition notLike(String left, String like);
public static Condition equals(String left, String right);
public static Condition equals(String left, Number right);
public static Condition equals(String left, Character right);
public static Condition between(String field, Object left, Object right);
public static ExistsCondition exists();
public static ExistsCondition exists(RecordModel model, String alias);
public static ExistsCondition exists(RecordModel model);
public static ExistsCondition notExists();
public static ExistsCondition notExists(RecordModel model, String alias);
public static ExistsCondition notExists(RecordModel model);
public static Condition notEquals(String left, Object right);
public static Condition greaterEquals(String left, Object right);
public static Condition greaterThan(String left, Object right);
public static Condition lesserThan(String left, Object right);
public static Condition lesserEquals(String left, Object right);
public static JoinClause leftJoin(RecordModel model, String alias, String Condition);
public static JoinClause leftOuterJoin(RecordModel model, String alias, String Condition);
public static JoinClause rightJoin(RecordModel model, String alias, String Condition);
public static JoinClause rightOuterJoin(RecordModel model, String alias, String Condition);
public static JoinClause innerJoin(RecordModel model, String alias, String Condition);

```

To add conditions to a Query object it must call the add method `(query.add(condition))`

**For example:**

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);

Query query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%"));

```

Every call of add method in object Query will put the Condition in a ordered list of conditions, that list will be joint as a AND condition.  So, if you add two conditions as follow:

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
 
Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

```

These two calls of add method will produce something like this:

name like 'Eleazar%' AND age = 33

Query class provides a method called `list()`, this method will execute a request using the *RecordModel* and *Conditions*. The response from Dynamicloud will be encapsulated in the object `RecordResults`

# Between condition

With this condition you can build selections like `age between 24 and 30` or `birthdate bewteen '2010-01-01 00:00:00' and '2015-11-01 23:59:59'`.

**A Between condition is composed by: field's identifier and an interval (left and right)**

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

Query query = provider.createQuery(new RecordModel(modelId));
query.add(Conditions.between("agefield", 20, 25));

RecordResults results = query.list();
.
.
.
```

**Using dates:**

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

Query query = provider.createQuery(new RecordModel(modelId));
query.add(Conditions.between("birthdate", "2010-01-01 00:00:00", "2015-11-01 23:59:59"));

RecordResults results = query.list();
.
.
.
```

# Exists condition

Exists condition is a way to execute correlated queries and get results if a specific condition is true.  For example, imagine the following SQL query:

```sql
-- Here we want to get the VIP users
SELECT * FROM users u WHERE EXISTS (SELECT * FROM vip_users v WHERE v.user_id = u.id)
```

**Let's do it using Dynamicloud's library:**

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

Query query = provider.createQuery(userModel);

/**
* This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
*/
query.setAlias("u");

ExistsCondition exists = Conditions.exists(vipmodel, "v");

/**
* The dollar symbols are to avoid to use right part as a String, but literally v.user_id = u.id
*/
exists.add(Conditions.equals("v.user_id", "$u.id$"));

query.add(exists);

try {
  RecordResults results = query.list();
  .
  .
  .
} catch (DynamicloudProviderException e) {
  //Oops!! What's wrong mister e?
}
```

**If you want to get the users without vip label:**

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

Query query = provider.createQuery(userModel);

/**
* This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
*/
query.setAlias("u");

ExistsCondition exists = Conditions.notExists(vipmodel, "v");

/**
* The dollar symbols are to avoid to use right part as a String, but literally v.user_id = u.id
*/
exists.add(Conditions.equals("v.user_id", "$u.id$"));

query.add(exists);

try {
  RecordResults results = query.list();
  .
  .
  .
} catch (DynamicloudProviderException e) {
  //Oops!! What's wrong mister e?
}
```

**Another capability in Exists condition is the JoinClauses to execute correlated queries with Joins, for example:**
```sql
SELECT * FROM users u WHERE EXISTS (SELECT * FROM vip_users v JOIN vip_country c ON c.vip_id = v.id WHERE v.user_id = u.id AND c.country_iso = 'BR')
```

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

Query query = provider.createQuery(userModel);

/**
* This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
*/
query.setAlias("u");

ExistsCondition exists = Conditions.exists(vipmodel, "v");

exists.join(Conditions.innerJoin(countryModel, "c", "c.vip_id = v.id"));

/**
* The dollar symbols are to avoid to use right part as a String, but literally v.user_id = u.id
*/
exists.add(Conditions.and(Conditions.equals("v.user_id", "$u.id$"), Conditions.equals("c.country_iso", "BR")));

query.add(exists);

try {
  RecordResults results = query.list();
  .
  .
  .
} catch (DynamicloudProviderException e) {
  //Oops!! What's wrong mister e?
}
```

# Join Clause

With Join Clause you can execute conditions and involve more than one model.  This is useful in situations when you need to compare data between two or more models and get information in one execution.

**A Join Clause is composed by: Model, Type, Alias and ON condition:**

```java
DynamicProvider<LangCountBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

/**
* Query has the main model 'user'
*/
Query<JoinResultBean> query = provider.createQuery(userModel);

try {
    /**
     * This is the alias to recordModel, this alias is necessary to use JoinClause
     */
    query.setAlias("user");

    /**
     * It is important the receptor bean to attach the results to it.  In this exemple, this bean must declare the userId and 
     * count methods.
     */
    recordModel.setBoundClass(LangCountBean.class);

    query.setProjection(new String[]{"user.id as userid", "count(1) as count"});
    
    /**
    * This is the model 'languages' to join with model 'user'
    */
    RecordModel languagesRecordModel = new RecordModel(...);
    
    /**
    * Conditions class provides: innerJoin, leftJoin, rightJoin, leftOuterJoin and rightOuterJoin.
    * If you need to add more than one join, you have to call query.join(...) and will be added in the query join list.
    *
    * This is an example to get the count of languages of every user.
    */
    query.join(Conditions.innerJoin(languagesRecordModel, "lang", "user.id = lang.userid"));

    /**
    * The Join Clause could be executed using a selection
    */
    query.add(Conditions.greaterThan("user.age", 25));
    
    /**
    * You can group the results to use sum, count, avg, etc.
    */
    query.groupBy("user.id");

    RecordResults<JoinResultBean> results = query.list();
    if (results.getFastReturnedSize() > 0) {
        JoinResultBean bean = results.getRecords().get(0);

        // Code here to manipulate the results
    }
} catch (DynamicloudProviderException ignore) {
}
```

# Next, Offset and Count methods

Query class provides a method to walk across the records of a Model.  Imagine a model with a thousand of records, obviously you shouldn't load the whole set of records, you need to find a way to load a sub-set by demand.

The method to meet this goal is `next`.  Basically, the next method will increase the offset automatically and will execute the request with the previous conditions. By default, offset and count will have 0 and 15 respectively.

**The uses of this method would be as a follow:**

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}

results = query.next();

//Loop with the next 15 records
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}
```

If you want to set an `offset` or `count`, follow this guideline:

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

//Every call will fetch max 10 records and will start from eleventh record.
query.setCount(10).setOffset(10);

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail();
}

//This call will fetch max 10 records and will start from twenty first record.
results = query.next();

//Loop through the next 10 records
for (ModelField item : results.getRecords()) {
  String email = item.getEmail();
}
```

# Order by

To fetch records ordered by a specific field, the query object provides the method `orderBy`.  To sort the records in a descending/ascending order you must call asc/desc method after call orderBy method.

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

//Every call will fetch max 10 records and will start from eleventh record.
query.setCount(10).setOffset(1).orderBy("email").asc(); // Here you can call desc() method

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}

```

# Group by and Projection

To group by a specifics fields, the query object provides the method `groupBy`.  To use this clause, you must set the projection to the query using `setProjection` method.

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

//Every call will fetch max 10 records and will start from eleventh record.
query.setCount(10).setOffset(10).orderBy("email").asc(); // Here you can call desc() method

// These are the fields in your projection
query.groupBy("name, email");
query.setProjection(new String[]{"name", "email"});

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}

```
# Functions as a Projection

Query object provides the setProjection method to specify the fields you want to fetch in a query.  In this method you can set the function you want to call. Every function must has an alias to bind it with a setMethod in BoundInstance object.

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);

query.add(Conditions.like("name", "Eleaz%"));
query.setProjection(new String[]{"avg(age) as average"});

ModelField instance = query.list().get(0);
Double average = instance.getAverage();
```

# Update using selection

There are situations where you need to update records using a specific selection.

In this example we are going to update the `name` where age > 24

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);

ModelField instance = new ModelField();
instance.setName("Eleazar");

provider.setBoundInstance(instance);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.greaterThan("age", 24));

/*
 This method will use the BoundInstance to get the data different than null (in this case the only data to use is **name**)
 and the query object to update only the records that match with the selection.
*/
provider.update(query);
```

# Delete using selection

There are situations where you need to delete records using a specific selection.

In this example we are going to delete the records where age > 24

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.greaterThan("age", 24));

/*
 This method will delete the records that match with the selection.
*/
provider.delete(query);
```
