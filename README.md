# Sharepoint Client

[SharePoint](https://products.office.com/en-au/sharepoint/collaboration) is a document management and collaboration tool developed by Microsoft used as a Content Management System.

## Requirements

- Create a [Sharepoint Add-in](https://docs.microsoft.com/en-us/sharepoint/dev/sp-add-ins/creating-sharepoint-add-ins-that-use-low-trust-authorization).
- Retrieve the Client ID, Client Secret, Domain and Principal for your [Add-In](https://docs.microsoft.com/en-us/sharepoint/dev/sp-add-ins/authorization-code-oauth-flow-for-sharepoint-add-ins#authorization-code-oauth-flow-for-add-ins-that-request-permissions-on-the-fly).

## Usage

```java
SharepointClient sharepointClient = new SharepointClient(apiKey, Region.SEA);
String accessToken = sharepointClient.getAccessToken(clientID, clientSecret, realm, principal, targetHost);
List<ExampleListItem> list = sharepointClient.getListItemsComplete(host, site, list, accessToken, ExampleListItem.class);
```

## Features

- [X] Get List Items by List Name
- [X] Get All List Items by List Name

## License

Sharepoint Client is available under the MIT license. [See LICENSE](https://github.com/philip-bui/sharepoint-client/blob/master/LICENSE) for details.