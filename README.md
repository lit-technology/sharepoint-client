# Sharepoint Client
[![Actions Status](https://github.com/philip-bui/sharepoint-client/workflows/build/badge.svg)](https://github.com/philip-bui/sharepoint-client/actions)

[SharePoint](https://products.office.com/en-au/sharepoint/collaboration) is a document management and collaboration tool developed by Microsoft used as a Content Management System.

## Requirements

- Create a [Sharepoint Add-in](https://docs.microsoft.com/en-us/sharepoint/dev/sp-add-ins/creating-sharepoint-add-ins-that-use-low-trust-authorization).
- Retrieve the Client ID, Client Secret, Domain and Principal for your [Add-In](https://docs.microsoft.com/en-us/sharepoint/dev/sp-add-ins/authorization-code-oauth-flow-for-sharepoint-add-ins#authorization-code-oauth-flow-for-add-ins-that-request-permissions-on-the-fly).

## Installation

### Maven

Set up Apache Maven to authenticate to GitHub Package Registry by editing your ~/.m2/settings.xml. For more information, see "[Authenticating to GitHub Package Registry](https://help.github.com/en/github/managing-packages-with-github-package-registry/configuring-apache-maven-for-use-with-github-package-registry#authenticating-to-github-package-registry)".

```xml
<dependencies>
  <dependency>
    <groupId>com.philipbui.sharepoint</groupId>
    <artifactId>sharepoint-client</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

### Gradle

Set up Gradle to authenticate to GitHub Package Registry by editing your build.gradle or build.gradle.kts file. For more information, see "[Authenticating to GitHub Package Registry](https://help.github.com/en/github/managing-packages-with-github-package-registry/configuring-gradle-for-use-with-github-package-registry#authenticating-to-github-package-registry)".

```gradle
dependencies {
    implementation("com.philipbui.sharepoint:sharepoint-client:1.0.0")
}
```

## Usage

```java
SharepointClient sharepointClient = new SharepointClient();
String accessToken = sharepointClient.getAccessToken(clientID, clientSecret, realm, principal, targetHost);
List<ExampleListItem> list = sharepointClient.getListItemsComplete(host, site, list, accessToken, ExampleListItem.class);
```

## Features

- [X] Get List Items by List Name
- [X] Get All List Items by List Name

## License

Sharepoint Client is available under the MIT license. [See LICENSE](https://github.com/philip-bui/sharepoint-client/blob/master/LICENSE) for details.