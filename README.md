# MakeMagic
The challenge here is to create a REST API that fulfils some requirements.

This application to offers CRUD functionalities for Harry Potter's characters.

### Endpoints available:

  - **/v1/public/character** - Can be accessed with Http request method GET to retrive a list of all characters.
  - **/v1/public/character?house={id}** - Can be accessed with Http request method GET to retrive a list of all characters related to a house Id.
  - **/v1/public/character** - Can be accessed with Http request method POST to create a character with the values specified in the payload.
  - **/v1/public/character/{id}** - Can be accessed with Http request method PATCH and PUT to update a character with the values specified in the payload.
  - **/v1/public/character/{id}** - Can be accessed with Http request method DELETE to delete a character with the Id specified.

This API has an integration with (https://www.potterapi.com/) to retrive the house Id for validation.

The payload format accepted to create and update a character is as following:

```
{
    "name": "Harry Potter",
    "role": "student",
    "school": "Hogwarts School of Witchcraft and Wizardry",
    "house": "5a05e2b252f721a3cf2ea33f",
    "patronus": "stag"
}
```
