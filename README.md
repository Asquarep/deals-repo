# ClusteredDataWarehouse
This application is a data warehouse to analyse FX deals and persist them to an Sql database.

## Requirement
To run this application, you will need docker installed. Additionally, you should have make installed also clone the repository.

#### Clone the Project
https://github.com/Ebube-1/clusteredDataWarehouse.git

#### Swagger Link
http://localhost:8080/swagger-ui/index.html

## Api Reference
#### Save FX Deal

|     Method    |         Http Request        |          Description           |
| ------------- |        -------------        |         -----------            |
|     post      |    api/v1/fx-deals/submit   |  Endpoint for saving fx deals  |

## Request Body
| Field Name    | Data Type     |
| ------------- | ------------- |
|   uniqueId    |    String     |
| fromCurrency  |    String     |
|  toCurrency   |    String     |
|    amount     |   BigDecimal  |

## Success Response
StatusCode 200

## Error Response
| Field Name    | Data Type     |
| ------------- | ------------- |
|     code      |    String     |
|    messages   |List of String |

You can find me on 
[![LinkedIn Badge](https://img.shields.io/badge/LinkedIn-Profile-informational?style=flat&logo=linkedin&logoColor=white&color=0D76A8)](https://www.linkedin.com/in/ebube-chineke/)
