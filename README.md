# Dynamicloud Java API
This Java API  helps you to use the power of Dynamicloud.  This API follows our Rest documentation to execute CRUD operations according to http methods.

# Getting started

This API has components to execute operations on [Dynamicloud](http://www.dynamicloud.org/ "Dynamicloud") servers.  The main components are the followings:

- RecordModel
- RecordCredential
- BoundInstance
- @Bind
- DynamicProvider
- Query
- Condition
- Conditions
- RecordResults

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
public class ModelFields implements BoundInstance
```

#Annotation @Bind
This is an annotation to Bind local attributes and fields in Dynamicloud.
Every set method must be annotated as a Bind method:
```java
public class ModelFields implements BoundInstance {
  private String email;
  
  @Bind(field = "email")
  public void setEmail(String email) {
    this.email = email;
  }
}
```

#DynamicProvider
**DynamicProvider** has important methods and can be used as follow:
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

ModelFields instance = new ModelFields();
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

With the Query object we can add conditions like EQUALS, IN, OR, AND, GREATER THAN, LESSER THAN, etc.  The query object is mutable and every call of its methods will return the same instance.

#Conditions class

This class has a set of methods to build conditions and add them in query object
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
 
Query query = provider.createQuery(model);
query.add(Conditions.like("name", "Eleaz%")).add(Conditions.equals("age", 33));

```

These two calls of add method will produce something like this:

**name like 'Eleazar%' AND age = 33**
