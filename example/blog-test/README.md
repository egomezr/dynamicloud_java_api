# Blog Test

This example is a test case to show how to use the Dynamicloud Java API.  The example is an interactive program that will ask the actions to create, list and delete records.

**Models that you need to create to test this example:**

####Model **'User'**

**Fields:**
- username (Textfield)
- email (Textfield)

####Model **'Blog'**

**Fields:**

- blogname (Textfield)
- blogdesc (Textarea)
- blogcat (Combobox), items:
  - Technology (tech)
  -	Medical (med)
  -	Science (sci)
  -	General (gen)
- createdt (Date)
- creatrid (Numeric)

####Model **'Post'**

**Fields:**
- postcontent (Textarea)
- posttitl (Textfield)
- blogid (Numeric)
- createdt (Date)
- creatrid (Numeric)


After creating models, you have to set the Client Secret Key (csk) and Application Client Id (aci) in source code berfore you compile it.

**The name of the class where you have to set the csk and aci is BlogManager:**

**Variables:**

```java
private static final String CSK = "csk#...";
private static final String ACI = "aci#...";
```

Now, execute the main method to start and test.
