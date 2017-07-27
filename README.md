DaVis
=================================

The initial idea of DaVis is to visualize data with some bonus features on top like categorization. 
 Cluster your data as you like, create your own charts and tables to get a nice overview about your data.
 The first use case i took was the visualization of finance data. It seems to be a good example for visualization. 
 Grouping/clustering those data seems quite easy. Think about what do you spend you money for?! Food, car, luxury, flat etc.
 But you also get money - salary, as a birthday present, price in lottery and so on. 
 DaVis should help to get an overview about all these additions and subtractions of your bank account.


Components
==========

To tell the truth, DaVis is (at least right now) just one component of the whole project. 
The other components are Kibana and ElasticSearch. 
Davis take its place as some kind of middleware. It aggregates given data and transfers it to ElasticSearch. After that 
you could use Kibana to create you charts. The major goal is to create a project that will include all those feature without 
 the need to handle all components (and upcoming components) separately.
 
 
#### Kibana
 Kibana is used to fetch the data from ElasticSearch and visualize them using different kind of charts, tables and so on.
 
#### ElasticSearch
 ElasticSearch is used as kind of the primary datastore. Its a search engine based on lucine the allows querying about big sets
 of data.
 
#### Davis as middleware
 DaVis itself is not really the "core-component" of all components, but the one that i write by my self ;) so i will spend
 most of the effort on that. DaVis' main task (at least right now) is to aggregate the given data. The first task is to 
 read CSV-files and convert them into JSON, that is consumable by ElasticSearch. The challenge at this point was not to
 use some CSV-libraries, put the files in and create a JSON for ElasticSearch. It is about the aggregation (more below)
 
 DaVis provides different possibilities to go with the given data. One (and actually the important one) is that it pushes all
  the data into ElasticSearch. Another possibility to view the (aggregated) data in a table, without using Kibana or ElasticSearch.
    That's a feature that will work right a way after checkout...
    
Aggregations
=======

Currently there is just one aggregation and that is also under construction right now, but anyway...

#### Categorization
All entries of the given data could be enriched with one (in the future also more) categories. That will all the user to
filter/search over those categories later on. 
An example: 
Lets imagine you provide the following categorization-mapping:

Aldi -> food
Lidl -> food
Netflix -> entertainment

this would allow you later on to create a chart that puts your expenses for food in relation to expenses for entertainment

You have to create one data-class to define the attributes that should be fetched from the datasource (atm only csv is suported)
and on the other hand if the category should be applied.


How to build
=======

As mentioned above, the external projects Kibana and Elasticsearch must be provided by your own (will change in future versions).
To build DaVis itself, you need to install SBT. It is used as the build-tool (like maven or gradle).

To build and run the project, just run __"sbt run"__

