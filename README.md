# 🖱️ Analytics Manager

A real-world **microservices** project built with **Apache Kafka** and **Spring Boot** that tracks button click events in real time. Demonstrates the Producer/Consumer messaging pattern using two completely independent services communicating only through Kafka.

---

## 📌 What This Project Does

Every time a user clicks a button on a webpage:

1. **Producer Service** captures the click and publishes an event to a Kafka topic
2. **Apache Kafka** stores and delivers the event
3. **Consumer Service** picks up the event, increments a counter, and exposes metrics
4. **Prometheus** scrapes the metrics
5. **Grafana** visualizes the click count in real time

```
[Browser Button] → [Producer Service] → [Kafka] → [Consumer Service] → [Prometheus] → [Grafana]
```

---

## 🏗️ Architecture

```
Analytics-Manager/
├── Producer/             → Spring Boot App (port 8080)
│    ├── Web page with button
│    ├── REST endpoint /api/click
│    └── Kafka Producer → publishes to "button-clicks" topic
│
└── Consumer/             → Spring Boot App (port 8081)
     ├── Kafka Consumer → listens to "button-clicks" topic
     ├── Micrometer Counter → tracks total clicks
     └── Prometheus endpoint → /actuator/prometheus
```

### Key Design Principle
The two services are **completely independent** — they share no code, no database, and no direct connection. They only communicate through Kafka. You can stop either service independently without affecting the other.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.4.3 | Application framework |
| Apache Kafka 4.2.0 | Message broker (KRaft mode) |
| Spring Kafka | Kafka integration for Spring |
| Micrometer | Metrics collection |
| Prometheus | Metrics storage & scraping |
| Grafana | Metrics visualization |
| Docker | Containerization |
| Gradle | Build tool |

---

## ✅ Prerequisites

Make sure you have these installed:

- Java 17+
- Gradle 9+
- Apache Kafka 4.x (KRaft mode — no Zookeeper needed)
- Docker
- Prometheus
- Grafana

---

## 🚀 Getting Started

### 1. Setup Kafka (KRaft Mode — No Zookeeper)

```powershell
# Generate a cluster ID
.\bin\windows\kafka-storage.bat random-uuid

# Format storage (replace <UUID> with your generated UUID)
.\bin\windows\kafka-storage.bat format -t <UUID> -c C:\kafka\kafka\config\server.properties --standalone

# Start Kafka
.\bin\windows\kafka-server-start.bat C:\kafka\kafka\config\server.properties
```

### 2. Create the Kafka Topic

```powershell
.\bin\windows\kafka-topics.bat --create --topic button-clicks --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

Verify it was created:
```powershell
.\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

### 3. Start Producer Service

```powershell
cd Producer
.\gradlew bootRun --no-configuration-cache
```

### 4. Start Consumer Service

```powershell
cd Consumer
.\gradlew bootRun --no-configuration-cache
```

### 5. Open the Web Page

Go to: **http://localhost:8080**

Click the button and watch the magic happen! 🎉

---

## 📡 How the Flow Works

```
User clicks button
       ↓
POST http://localhost:8080/api/click
       ↓
ClickController → ClickEventProducer
       ↓
KafkaTemplate.send("button-clicks", "CLICK::2026-03-13T...")
       ↓
Kafka stores message in "button-clicks" topic
       ↓
@KafkaListener in ClickEventConsumer fires
       ↓
clickCounter.increment()
       ↓
Prometheus scrapes http://localhost:8081/actuator/prometheus
       ↓
Grafana displays button_clicks_total 📊
```

---

## 📊 Setting Up Prometheus

Edit `prometheus.yml` and add the consumer service as a scrape target:

```yaml
scrape_configs:
  - job_name: "button-analytics"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["localhost:8081"]
    scrape_interval: 5s
```

Start Prometheus:
```powershell
cd C:\prometheus
.\prometheus.exe
```

Verify at: **http://localhost:9090**

---

## 📈 Setting Up Grafana

1. Start Grafana and open **http://localhost:3000** (admin/admin)
2. Go to **Connections → Data Sources → Add → Prometheus**
3. Set URL to `http://localhost:9090` → Save & Test
4. Create a new Dashboard → Add Panel
5. Use this query:
```
button_clicks_total
```
6. Set visualization to **Stat** or **Time Series**

---

## 🖥️ Running All Services

You need **3 terminals** open simultaneously:

| Terminal | Command |
|---|---|
| Terminal 1 | Kafka server |
| Terminal 2 | `.\gradlew bootRun` in Producer |
| Terminal 3 | `.\gradlew bootRun` in Consumer |

Plus Prometheus and Grafana running in the background.

---

## 📋 API Endpoints

### Producer Service (port 8080)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Web page with click button |
| POST | `/api/click` | Records a button click event |

### Consumer Service (port 8081)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/actuator/prometheus` | Prometheus metrics |
| GET | `/actuator/health` | Service health check |

---

## 💡 Key Concepts Demonstrated

**Producer/Consumer Pattern**
The producer publishes events without knowing who will consume them. The consumer listens without knowing who produced the events. Pure decoupling.

**Kafka Topics**
The `button-clicks` topic acts as a persistent log. Messages are stored even if the consumer is temporarily down — they'll be processed when it comes back up.

**Offset Management**
Kafka tracks where the consumer left off. If the consumer crashes and restarts, it picks up exactly where it stopped — no clicks are lost.

**Metrics Pipeline**
Consumer → Micrometer Counter → Prometheus scrape → Grafana visualization. A complete observability pipeline.

---

## 🔧 Configuration

### Producer Service `application.properties`
```properties
spring.application.name=producer-service
server.port=8080
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
```

### Consumer Service `application.properties`
```properties
spring.application.name=consumer-service
server.port=8081
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=click-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
management.endpoints.web.exposure.include=prometheus,health
```

---

## 📚 What I Learned Building This

- How Apache Kafka works as a message broker
- The difference between Kafka and traditional REST communication
- How to set up Kafka in KRaft mode (no Zookeeper)
- Producer/Consumer microservice pattern
- How Micrometer, Prometheus, and Grafana work together
- Why loose coupling between services matters in real systems

---

## 🤝 Author

Built as a learning project to understand Kafka-based microservice communication.
