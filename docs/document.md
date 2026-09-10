- Project : white-vault
- Author : Karrol
- Platform : Android
- Language : Java
- Architecture : MVVM
- Project Type : Android Security / Utility Application
- Date : 10-9-2026



Overview
========

People often store important documents, personal files, and passwords in different locations such as their mobile storage, computers, cloud services or notes applications. Managing information across multiple locations can make it difficult to find and organize important data when needed. It can also increase the risk of losing sensitive information, accidentally sharing it, or allowing unauthorized people to access it. Passwords may also be stored in insecure places, such as plain text notes or unprotected files, which can create serious security risks.

This application works as an encrypted virtual vault for securely storing important documents and passwords on an Android device. Instead of carrying physical identification documents or keeping passwords in unsecured places.

The application is designed to keep sensitive information organized and protected in one secure location. Documents can be digitized and stored within the vault, while passwords can be stored securely with the corresponding username/email. Encryption helps protect the stored information from unauthorized access, even if someone gains access to the device's storage.

By providing a single, secure location for documents and passwords, the application makes it easier for users to manage their important information while reducing the risks associated with carrying physical documents or storing sensitive data in insecure locations.



Objectives
==========

- Securely store documents and passwords.
- Protect data using encryption.
- Provide user authentication.
- Allow users to search and manage saved information.
- Provide a simple and user-friendly interface.



Requirements
============

- Functional Requirements
-------------------------
    Functional requirements describe the features and operations that the application provides to users.
    
    1. User Authentication
        * The application shall allow users to create and configure a secure login method.
        * The application shall allow users to unlock the application using the configured authentication method.
        * The application shall prevent unauthorized users from accessing stored information.
        
    2. Password Management
        * The application shall allow users to add and store passwords securely.
        * Users shall be able to view, edit, and delete saved password entries.
        * Users shall be able to store information such as service name, username, password, and notes.
        * The application shall provide a search facility for stored passwords.
        * Passwords shall not be displayed unnecessarily.
        
    3. Document Management
        * The application shall allow users to add documents or images to the secure locker.
        * Users shall be able to view stored documents.
        * Users shall be able to delete documents from the locker.
        * Users shall be able to organize or identify documents using names or categories.
    
    4. Security and Data Protection
        * The application shall protect sensitive information stored within the locker.
        * Password related data shall be protected using appropriate cryptographic techniques.
        * The application shall prevent unauthorized access to protected content.
        * Sensitive information should not be exposed unnecessarily in application logs or user interfaces.
        
- Non-Functional Requirements
-----------------------------
    Non-functional requirements describe the quality, performance, security, and usability characteristics of the application.
    
    1. Security
        * The application should provide strong protection for sensitive user information.
        * Stored sensitive data should be encrypted.
        * Authentication credentials should not be stored as plain text.
        * The application should minimize the exposure of sensitive information.
        
    2. Privacy
        * User data should remain private and should not be unnecessarily shared with third parties.
        * The application should request only the permissions required for its functionality.
        * Sensitive information should not be unnecessarily transmitted over a network.
        
    3. Performance
        * The application should start and respond quickly during normal operation.
        * Searching and retrieving stored items should be efficient.
        * The application should use device resources such as CPU, memory, and storage efficiently.
        
    4. Usability
        * The application should provide a simple and easy-to-understand user interface.
        * Common operations such as adding, viewing, editing, and deleting items should require minimal steps.



Functionalities
===============
    The application provides a secure and centralized environment for managing personal documents and passwords on an Android device. 
    The functionalities of the application are described below.
    
    1. User Authentication
        User authentication is the first layer of protection for the application. It ensures that only an authorized user can access the stored documents and passwords.
        
        * Users can create and configure an application login method.
        * Users must authenticate before accessing the locker.
        * The application prevents unauthorized access to stored information.
        * Authentication related information is handled securely.
        
    2. Document Management
        The Document Management functionality allows users to store and manage important documents and images inside the application.
        
        * Users can add documents or images to the locker.
        * Users can provide a name or description for stored documents.
        * Users can view stored documents.
        * Users can open documents when required.
        * Users can delete unwanted documents.
        
    3. Password Management
        The Password Management functionality provides a centralized location for storing and managing different account credentials.
        
        * Users can add new password entries.
        * Users can view saved password entries after authentication.
        * Users can edit existing password entries.
        * Users can delete password entries.
        * Password information is protected from unnecessary exposure.
        
    4. Encryption and Security
        Encryption and security functionality protects sensitive information stored by the application.
        
        * Password related information is protected from being stored or exposed as plain text where applicable.
        * Cryptographic techniques are used where required for protecting sensitive data.
        * Access to protected information is restricted through authentication.
        * The application minimizes unnecessary exposure of sensitive information.
        * Sensitive data should not be unnecessarily displayed in logs or application interfaces.



Technical Implementation
========================

    1. Project Structure
        
        white-vault
        |
        |- app/
        |    |- src/
        |    |    |- main/
        |    |        |- java/
        |    |        |    |- com.white_vault.app/
        |    |        |        - MainActivity.java
        |    |        |        - Cryptographic.java
        |    |        |        - AuthenticationActivity.java
        |    |        |
        |    |        |-res/
        |    |        |    |- drawable/
        |    |        |    |    - logo.png
        |    |        |    |
        |    |        |    |- layout/
        |    |        |    |    - activity_main.xml
        |    |        |    |    - activity_authentication.xml
        |    |        |    |
        |    |        |    |- mipmap/
        |    |        |    |- values/
        |    |        |        - color.xml
        |    |        |        - string.xml
        |    |        |
        |    |        |- AndroidManifest.xml
        |    |
        |    |- build.gradle
        |
        |- gradle/
        |- build.gradle.kts
        |- settings.gradle.kts
        |- gradlew
        |- gradlew.bat
        |- gradle.properties
        |- local.properties
        
        ** The exact structure may vary depending on the Android architecture **
        
    2. Authentication Module
        The Authentication Module manages access to the application and protects the locker from unauthorized users.
        
        A simplified authentication flow is:

        User
         ↓
        Enter Authentication Information
         ↓
        Authentication Validation
         ↓
         ┌───────────────┐
         │     Valid?    │
         └───────────────┘
            ↓ Yes       ↓ No
         Unlock App    Show Error
            ↓
        Access Locker
        
    3. Document Module
        The Document Module is responsible for managing documents and images stored within the locker.
        
        A typical document operation follows:

            Select Document
                   ↓
            Validate File
                   ↓
            Process / Protect File 
                   ↓
            Store Document
                   ↓
            Save Metadata
                   ↓
            Display in Locker
            
        Document metadata may include information such as the document name, file type, location, creation date, and category.
        
    4. Password Module
        The Password Module manages account credentials stored by the user.

        A password entry may contain:
            - Service Name
            - Username
            - Password
            - Notes
            
    5. UI Implementation
        The UI Implementation provides the screens through which users interact with the application.

        The interface can be divided into several major screens, such as:
            - Login / Authentication Screen
            - Home / Dashboard
            - Password List
            - Add Password
            - Password Details
            - Document List
            - Add Document
            - Document Details / Viewer
            - Settings
        
        A simplified navigation flow is:

            Authentication
                  ↓
                Home
               ↙    ↘
        Passwords  Documents
           ↓          ↓
        Details     Details
           ↓          ↓
        Edit/Delete  View/Delete
        
        
        
Future Enhancements
===================

Although the current version provides the core functionality required for managing passwords and documents, several additional features can be introduced in future versions to improve security, usability, and reliability.
    
    
    
Conclusion
==========

The White Vault application addresses the problem of storing sensitive information in multiple locations by providing a single locker where users can manage their digital documents and account credentials. The system includes essential functionalities such as user authentication, document management, password management and security mechanisms.
    
