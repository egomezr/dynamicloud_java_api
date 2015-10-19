# dynamicloud_java_api
This Java API  helps you to use the power of Dynamicloud.  This API follows our Rest documentation to execute CRUD operations according to http methods.

# Getting started

This API contents components to execute operations over [Dynamicloud](http://www.dynamicloud.org/ "Dynamicloud") servers.  The main components are the followings:

- DynamicProvider
- RecordCredential
- RecordModel
- RecordResults
- @Bind
- BoundInstance
- Condition
- Conditions
- RecordQuery

These components will allow you to connect on Dynamicloud servers, authenticate and execute operations like *loadRecord*, *updateRecord*, *deleteRecord*, *get record's information according to selection*, *get record's information according to projection*, etc.  The next step is explain every components and how to execute operations.  

###DynamicProvider
```java
public class DynamicProviderImpl<T> implements DynamicProvider<T>
```

**DynamicProvider** has important methods and can be used as follow:
 
**First, let's explain the constructor of this class:**
 
 ```java
public DynamicProviderImpl(RecordCredential credential)
 ```
This constructor receives an object with the credential to gain access.  The credential object is composed of Client Secret Key (CSK) and Application Client ID (ACI), these keys were provided at moment of your registration.
 
 #### Methods
 **Load Record**
```java
public T loadRecord(Long rid, RecordModel model, Class boundClass)
```
This method loads a record according to rid *(RecordID)* in model *(RecordModel)* the third parameter is the bound class that will contain the data of this record.  The generic type *'T'* must be the type related to boundClass parameter.

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(...);
 
ModelField instance = provider.loadRecord(2l, model, ModelField.class);
```

**Update Record**
 ```java
public void updateRecord(RecordModel model, T instance)
```
This method updates the record (instance) that implements BoundInstance interface and must be the same type from provider's generic type..

**For example, a call of this method would be:**
 ```java
DynamicProvider<ModelField> provider = new DynamicProviderImpl<ModelField>(...);
 
ModelField instance = provider.loadRecord(2l, model, ModelField.class);
instance.setEmail("email@dynamicloud.org");
 
provider.updateRecord(model, instance);
```
