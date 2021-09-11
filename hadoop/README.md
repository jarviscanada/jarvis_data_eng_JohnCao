# Hadoop Project
Table of Contents
* [Introduction](#Introduction)
* [Hadoop Cluster](#Hadoop-Cluster)
* [Hive Project](#Hive-Project)
* [Improvements](#Improvements)

## Introduction
The Hadoop project allows the user/team to process big data using the Apache Hadoop ecosystem. Various components and tools are evaluated including MapReduce, HDFS, YARN, and Hive.
A Hadoop Cluster was provisioned using Google Cloud Platform (GCP) consisting of one master node and two worker nodes. 
A Zeppelin notebook was used to run Apache Hive queries (HiveQL) on the World Development Indicators (WDI) dataset for 2016 provided by the World Bank. 

## Hadoop Cluster
![hadoop-cluster](assets/cluster.png)
### Hardware specifications
Cluster consists of one master node and two worker nodes. Each node has the following specifications:
- 2 CPUs
- 12GB RAM (memory)
- 100GB of disk space
### Big Data Tools Evaluated
- `Hadoop Distributed File System (HDFS)`: Hadoop's distributed file system which distributes the storage of very large data/files among cluster of nodes and maintains redundant copies of data. 
  Individual files are broken into small chunks of fixed size, known as blocks. These blocks are stored across a cluster of one or more nodes, referred to as DataNodes. All the metadata for the filesystem is handled by a single node called the NameNode.
  When a client wants to access a file, the client will contact the NameNode to retrieve a list of locations that identify the DataNodes holding the blocks that comprise the file. In this cluster, there is one NameNode and two DataNodes.


- `Yet Another Resource Negotiator (YARN)`:  YARN manages the resources of the Hadoop cluster. It seperates the management of resources from the coordination of tasks.


- `MapReduce`: A programming paradigm desgined to process large data sets in a distributed systems. It consists of three main components: a mapper, a partitioner, and a reducer.


- `Apache Hive`: Hive is a tool for performing distributed processing and data analysis. It offers an SQL-like query language (HiveQL) that makes it easier to perform data analysis on Hadoop.


- `Zeppelin`: A web-based notebook for developing, executing, and sharing data code and visualizing results without the need for the command line. It supports a list of languages such as Hive, Scala, and SparkSQL.

## Hive Project
Hive queries can be optimized using the following strategies and tools:
- `Partitions`: A partition allows Hive to scan a subset of the data instead of having to scan the entire table.
- `Columnar File`: A columnar file stores data by column and not by row. This allows for faster retrieval of data when only some columns are required. In this project, the data contained in a CSV file was converted to a parquet file.

![zeppelin-notebook](assets/zeppelin.png)

## Improvements
- Increase the number of worker nodes
- Use another execution engine over MapReduce such as Apache Spark
- Store dataset as a columnar file in HDFS instead of CSV
