# Dynamicloud Java API v1.0.0
This Java API  helps you to use the power of Dynamicloud.  This API follows our Rest documentation to execute CRUD operations according to http methods.

#Requirements

Java JDK 7 and later, you can download it on [Java Oracle site](http://www.oracle.com/technetwork/java/javase/downloads/index.html "Download Java")

#Main files

- **dist/dynamicloud-1.0.0.jar**
- **Dependencies are in lib folder**
- **example/blog-test**

#Javadoc

To read the java documentation click [here](http://www.dynamicloud.org/ "Dynamicloud Java documentation")

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
  - [Next, Offset and Count methods](#next-offset-and-count-methods)
  - [Order by](#order-by)
  - [Group by and Projection](#group-by-and-projection)
  - [Functions as a Projection](#functions-as-a-projection)
7. [Update using selection](#update-using-selection)
8. [Delete using selection](#delete-using-selection)

These components will allow you to connect on Dynamicloud servers, authenticate and execute operations like *loadRecord*, *updateRecord*, *deleteRecord*, *get record's information according to selection*, *get record's information according to projection*, etc.  The next step is explain every components and how to execute operations.  

#RecordModel
This class represents a model in Dynamicloud.  Every method in this API needs a model to identify a specific set of records and execute operations on them.

To instantiate a model you have to pass an ID from your models in Dynamicloud
```java
RecordModel recordModel = new RecordModel(modelId);
```

#RecordCredential
This class encapsulates the API keys to gain access on Dynamicloud servers.
```java
RecordCredential recordCredential = new RecordCredential(CSK, ACI);
```

#BoundInstance
This interface declares the required methods for a bound instance
```java
public interface BoundInstance
```

A bound instance must implement BoundInstance interface to be eligible to use in DynamicProvider methods.
```java
public class ModelField implements BoundInstance
```

#Annotation @Bind
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

#DynamicProvider
**DynamicProvider** provides important methods and can be used as follow:
```java
public class DynamicProviderImpl<T> implements DynamicProvider<T>
```
 
**First, let's explain the constructor of this class:**
 ```java
public DynamicProviderImpl(RecordCredential credential)
 ```
This constructor receives an object with the credential to gain access.  The credential object is composed of Client Secret Key (CSK) and Application Client ID (ACI), these keys were provided at moment of your registration.
 
#Methods
 
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
instance.setBirthdate("1982-05-21");

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

#Query class

This class provides a set of methods to add conditions, order by and group by clauses, projections, etc.

```java
public Query add(Condition condition);
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

#RecordResults

**This class provides three methods:**
- **getTotalRecords:** The total records in RecordModel
- **getFastReturnedSize:** The returned size of records that have matched with Query conditions
- **getRecords:** A list of records, the objects in this list will be **BoundInstances** according to Query's generic type.

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

#Conditions class

This class provides a set of methods to build conditions and add them in query object
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
public static Condition notEquals(String left, Object right);
public static Condition greaterEquals(String left, Object right);
public static Condition greaterThan(String left, Object right);
public static Condition lesserThan(String left, Object right);
public static Condition lesserEquals(String left, Object right);

```

To add conditions to a Query object it must call the add method **(query.add(condition))**

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

name like 'Eleazar%' **AND** age = 33

Query class provides a method called **list()**, this method will execute a request using the *RecordModel* and *Conditions*. The response from Dynamicloud will be encapsulated in the object **RecordResults**

#Next, Offset and Count methods

Query class provides a method to walk across the records of a Model.  Imagine a model with a thousand of records, obviously you shouldn't load the whole set of records, you need to find a way to load a sub-set by demand.

The method to meet this goal is **next**.  Basically, the next method will increase the offset automatically and will execute the request with the previous conditions. By default, offset and count will have 0 and 15 respectively.

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

If you want to set an **offset** or **count**, follow this guideline:

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

//Every call will fetch max 10 records and will start from eleventh record.
query.setCount(10).setOffset(1);

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}

//This call will fetch max 10 records and will start from twenty first record.
results = query.next();

//Loop through the next 10 records
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}
```

#Order by

To fetch records ordered by a specific field, the query object provides the method **orderBy**.  To sort the records in a descending/ascending order you must call asc/desc method after call orderBy method.

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

#Group by and Projection

To group by a specifics fields, the query object provides the method **groupBy**.  To use this clause, you must set the projection to the query using **setProjection** method.

```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(recordCredential);
recordModel.setBoundClass(ModelField.class);

Query<ModelField> query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

//Every call will fetch max 10 records and will start from eleventh record.
query.setCount(10).setOffset(1).orderBy("email").asc(); // Here you can call desc() method

// These are the fields in your projection
query.groupBy("name, email");
query.setProjection(new String[]{"name", "email"});

RecordResults results = query.list();
for (ModelField item : results.getRecords()) {
  String email = item.getEmail():
}

```
#Functions as a Projection

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

#Update using selection

There are situations where you need to update records using a specific selection.

In this example we are going to update the **name** where age > 24

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

#Delete using selection

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
