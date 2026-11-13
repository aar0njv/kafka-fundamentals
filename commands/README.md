
# Apache Kafka

This directory contains the hands-on commands for Kafka container management, topic creation and message production/consumption using the native KRaft broker.

## Container Management

| Action | Command |
| :--- | :--- |
| **Start Kafka Broker** | `docker run -d --name kafka-container -p 9092:9092 apache/kafka:3.9.0` |
| **View Logs** | `docker logs -f kafka-container` |
| **Open Container Shell** | `docker exec -it kafka-container bash` <br> *(Followed by `cd /opt/kafka/bin`)* |
| **Tear Down** | `docker rm -f kafka-container` |


## 1. Topic Management

Create the `order-events` topic with 3 partitions and a replication factor of 1.

```bash
./kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic order-events \
  --partitions 3 \
  --replication-factor 1

# Describe the structure of order-events
./kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic order-events
```

- by setting partition to 3, upto 3 consumers can read and process messages simultaneously, increasing scalability <br>

- replication factor determines how many copies of each partition should be maintained, promotes fault tolerance

### Produce Keyed Messages
Create keyed messages in `order-events` by setting `parse.key=true`

```bash
./kafka-console-producer.sh --bootstrap-server localhost:9092 \
--topic order-events \
--property parse.key=true \
--property key.separator=:
```

then add messages in the form (CTRL + C to exit)
```bash
>1001:{"type":"CREATED","amount":"50.00"}
>1002:{"type":"CREATED","amount":"20.00"}
>1001:{"type":"SHIPPED"}
>1001:{"type":"DELIVERED"}
>1002:{"type":"SHIPPED"}
```
- Hash key helps in grouping all the messages with same key to the same partition
- **Partition-Level Ordering** : Because all events for order `1001` are forced into same partition, they are appended in the exact sequence that they arrive.
- NO KEY means default `Round Robin Scheduling` distribution to balance load, elminatiing the ordering guarantee.

### Create a Consumer Group 

```bash
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-events \
--group lab-group --from-beginning \
--property print.key=true --property print.partition=true --property print.offset=true
```
- `--from-beginning`: used when creating a brand new consumer group to consume all the messages from the beginning even if it was consumed by another group. Else the new consumer group just wait for new messages to be produced.

**Output** :
```bash
Partition:1     Offset:0        1002    {"type":"CREATED","amount":"20.00"}
Partition:0     Offset:0        1001    {"type":"CREATED","amount":"50.00"}
Partition:0     Offset:1        1001    {"type":"SHIPPED"}
Partition:0     Offset:2        1001    {"type":"DELIVERED"}
Partition:1     Offset:1        1002    {"type":"SHIPPED"}
```

### NOTE: What Happens if i create another consumer in the same consumer-group?
Kafka rebalances the load and assign partition 0 to one consumer and partition 1 to the other to split the load. If you create one more then that consumer will sit idle because there is only 2 partitions  

### Reset Offset to start

```bash
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--group lab-group \
--reset-offsets --to-earliest \
--topic order-events --execute
```
### Describe Consumer Group

```bash
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--describe --group lab-group
```

### NOTE: What is Lag?
If the consumer was stopped and new messages where produced by the producer, and then when we describe the consumer-group, we can see the **LAG** what is how many messages are produced yet note consumed by the consumer.


### Delete Consumer Group

```bash
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--delete --group analytics-history-group
```