# 🔍 Full-Stack Document Search Engine

A modern, enterprise-grade document search application built with **Spring Boot**, **React**, and **OpenSearch**.

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![OpenSearch](https://img.shields.io/badge/OpenSearch-1.3.14-yellow.svg)](https://opensearch.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

## 🚀 **Tech Stack**

| Layer | Technology | Version |
|-------|------------|---------|
| **Backend** | Spring Boot | 2.7.18 |
| **Frontend** | React | 18.2.0 |
| **Search Engine** | OpenSearch | 1.3.14 |
| **Language** | Java | 8+ |
| **Build Tool** | Maven | 3.8+ |
| **Package Manager** | npm | 9+ |
| **Containerization** | Docker | Latest |

## ⚡ **Features**

### 🔍 **Search Capabilities**
- **Real-time search** with 300ms debouncing
- **Fuzzy matching** for typos and partial matches
- **Multi-field search** across title and content
- **Instant results** with sub-100ms response times

### 📝 **Document Management**
- **Create documents** with unique IDs
- **Index validation** with comprehensive error handling
- **RESTful APIs** following industry standards
- **Health monitoring** for production deployment

### 🎨 **Modern UI/UX**
- **Responsive design** for all screen sizes
- **Loading states** and **error handling**
- **Search suggestions** and **result highlighting**
- **Clean, professional interface**

## 🏗️ **Architecture**

- **Frontend (React), Backend (Spring Boot), Search Engine**

- **Port 3000 → Port 8080 → OpenSearch 9200**


### **System Flow**
1. **User Interface** → React application with real-time search
2. **API Layer** → Spring Boot REST endpoints with validation
3. **Search Engine** → OpenSearch with custom indexing and querying
4. **Data Flow** → JSON-based communication with error handling

## 🛠️ **Quick Start**

### **Prerequisites**
- **Java 8+**
- **Node.js 18+**
- **Docker**
- **Maven 3.8+**

### **1. Clone Repository**
- **git clone https://github.com/ishand3thrx/document-search.git**
- **cd fullstack-document-search**


### **2. Start OpenSearch**
docker run -d --name opensearch-node
-p 9200:9200 -p 9600:9600
-e "discovery.type=single-node"
-e "plugins.security.disabled=true"
-e "OPENSEARCH_JAVA_OPTS=-Xms512m -Xmx512m"
opensearchproject/opensearch:1.3.14



### **3. Start Backend**
- **cd backend**
- **mvn spring-boot:run**



### **4. Start Frontend**
- **cd frontend**
- **npm install**
- **npm start**



### **5. Access Application**
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **OpenSearch**: http://localhost:9200

## 📡 **API Documentation**

### **Base URL**: `http://localhost:8080/api`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/index` | Index a new document | `{"id": "doc1", "title": "Title", "content": "Content"}` |
| `GET` | `/search?q={query}&limit={limit}` | Search documents | None |
| `GET` | `/health` | Health check | None |

