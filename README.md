
# Project Title

A brief description of what this project does and who it's for

<div align="center">

# ☁️ Coding Cloud

**An Ed-Tech Platform for IT Training — Built with a Full Production-Grade DevOps Stack**

[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=white)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-5-646CFF?logo=vite&logoColor=white)](https://vitejs.dev/)
[![Node.js](https://img.shields.io/badge/Node.js-18-339933?logo=node.js&logoColor=white)](https://nodejs.org/)
[![Express](https://img.shields.io/badge/Express-4-000000?logo=express&logoColor=white)](https://expressjs.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Orchestrated-326CE5?logo=kubernetes&logoColor=white)](https://kubernetes.io/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939?logo=jenkins&logoColor=white)](https://www.jenkins.io/)
[![Terraform](https://img.shields.io/badge/Terraform-AWS-7B42BC?logo=terraform&logoColor=white)](https://www.terraform.io/)
[![AWS](https://img.shields.io/badge/AWS-ap--south--1-FF9900?logo=amazonaws&logoColor=white)](https://aws.amazon.com/)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Directory Structure](#-directory-structure)
- [Frontend](#-frontend)
- [Backend](#-backend)
- [Docker](#-docker)
- [CI/CD — Jenkins](#-cicd--jenkins)
- [Kubernetes](#-kubernetes)
- [Infrastructure — Terraform (AWS)](#-infrastructure--terraform-aws)
- [Environment Variables](#-environment-variables)
- [Placeholders](#-placeholders)
- [Getting Started (Local)](#-getting-started-local)

---

## 🎯 Overview

**Coding Cloud** is an IT training institute web platform where students can:

- Browse courses (AWS/DevOps, MERN, Python, Java, Data Science, Databases)
- View detailed course pages with curriculum highlights
- Submit an enquiry form that is stored in a MySQL database

The project is designed as a **full production-grade application** with a complete DevOps pipeline: containerised services, Kubernetes orchestration, Jenkins CI/CD, and AWS infrastructure provisioned via Terraform.

---

## 🏗️ Architecture

```
                         Internet
                             │
                    ┌────────▼────────┐
                    │   AWS ALB (80)  │  ← Public-facing Load Balancer
                    └────────┬────────┘
                             │ NodePort :31000
                    ┌────────▼────────┐
                    │  Nginx Service  │  ← K8s reverse proxy (ConfigMap)
                    └────┬───────┬───┘
                         │       │
           ┌─────────────▼─┐   ┌─▼─────────────────┐
           │ Frontend Pods │   │   Backend Pods      │
           │ (React → Nginx│   │   (Express API)     │
           │   port 80)    │   │   port 5000         │
           └───────────────┘   └──────────┬──────────┘
                                          │
                              ┌───────────▼───────────┐
                              │  AWS RDS MySQL 8.0     │
                              │  (Private Subnet)      │
                              └───────────────────────┘

CI/CD Flow:
  GitHub (master branch)
      └─► Jenkins → Docker Build Agent → Docker Hub 
                  → kubectl on K8s Master → Rolling Deployment
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Frontend** | Vite 5, React 18, TypeScript 5, Tailwind CSS v3, shadcn/ui (Radix UI) |
| **Backend** | Node.js 18, Express 4, mysql2 |
| **Database** | MySQL 8.0 (AWS RDS `db.t3.micro`) |
| **Containerization** | Docker (multi-stage build for frontend, single-stage for backend) |
| **Container Registry** | Docker Hub |
| **Orchestration** | Kubernetes (self-managed cluster on EC2), Nginx reverse proxy |
| **Autoscaling** | Kubernetes HPA — CPU-based (all 3 deployments) |
| **CI/CD** | Jenkins — 2 separate change-set-gated pipelines |
| **Infrastructure (IaC)** | Terraform — AWS VPC, EC2, RDS, ALB, NAT, Security Groups |
| **Cloud Region** | AWS `ap-south-1` (Mumbai) |

---

## 📁 Directory Structure

```
Shiftotech-Project/
│
├── frontend/                   # React SPA (Vite + TypeScript)
│   ├── src/
│   │   ├── pages/              # Route-level page components
│   │   ├── components/         # Shared UI components + shadcn/ui
│   │   ├── hooks/              # Custom React hooks
│   │   └── lib/                # Utility functions
│   ├── Dockerfile              # Multi-stage: build → nginx:alpine
│   ├── vite.config.ts
│   ├── tailwind.config.ts
│   └── package.json
│
├── backend/                    # Express REST API (Node.js)
│   ├── server.js               # Main server file with all routes
│   ├── Dockerfile              # node:18-alpine with healthcheck
│   ├── .env                    # DB connection details
│   └── package.json
│
├── Jenkins/
│   ├── Frontend-Jenkinsfile.groovy   # Frontend CI/CD pipeline
│   └── Backend-Jenkinsfile.groovy    # Backend CI/CD pipeline
│
├── k8s/
│   ├── namespace.yml               # coding-cloud namespace
│   ├── frontend-deployment.yml     # Frontend Deployment + ClusterIP Service
│   ├── backend-deployment.yml      # Backend Deployment + ClusterIP Service
│   ├── nginx-deployment.yml        # Nginx Deployment + NodePort Service (:31000)
│   ├── configmap.yml               # Nginx reverse proxy config
│   └── HPA.yml                     # HorizontalPodAutoscaler (all 3 services)
│
└── Terraform/
    ├── terraform.tf                # Root module — wires vpc, ec2, rds
    ├── provider.tf                 # AWS provider config
    ├── vpc/
    │   ├── vpc.tf                  # VPC, subnets, IGW, NAT, route tables
    │   ├── security-group.tf       # SGs for ALB, Jenkins, Docker, K8s, RDS
    │   └── output.tf               # Outputs (subnet IDs, SG IDs, VPC ID)
    ├── ec2/
    │   ├── jenkins-docker-vm.tf    # Jenkins (public) + Docker (private) EC2
    │   ├── cluster-vm.tf           # K8s Master + Worker EC2 (private)
    │   ├── alb.tf                  # Application Load Balancer → NodePort 31000
    │   ├── key-pair.tf             # SSH key pair resource
    │   └── variable.tf             # Input variables from vpc module
    └── rds/
        ├── rds.tf                  # MySQL 8.0 RDS in private subnet group
        └── variable.tf             # Input variables from vpc module
```

---

## 🖥️ Frontend

The frontend is a **Single Page Application** built with **Vite + React + TypeScript**.

### Pages & Routes

| Route | Component | Description |
|---|---|---|
| `/` | `Index.tsx` | Homepage — hero section + 6 course cards |
| `/enquiry` | `Enquiry.tsx` | Contact/enquiry submission form |
| `/courses/:slug` | `CourseDetail.tsx` | Per-course detail page |
| `*` | `NotFound.tsx` | 404 fallback |

### Key Libraries

| Library | Purpose |
|---|---|
| `react-router-dom` v6 | Client-side routing |
| `@tanstack/react-query` v5 | Server state management |
| `react-hook-form` + `zod` | Form validation |
| `shadcn/ui` (Radix UI) | Accessible component primitives |
| `tailwindcss` v3 | Utility-first styling |
| `lucide-react` | Icon set |
| `recharts` | Data charts |
| `sonner` | Toast notifications |
| `next-themes` | Light/dark theme support |
| `embla-carousel-react` | Carousel/slider |

### Dev Server

```bash
cd frontend
npm install
npm run dev        # starts at http://localhost:8080
```

---

## ⚙️ Backend

A minimal **Express REST API** that persists student enquiries to MySQL.

### API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/health` | Health check — used by K8s liveness/readiness probes |
| `POST` | `/api/enquiry` | Submit a new enquiry |
| `GET` | `/api/enquiries` | List all enquiries (admin) |
| `GET` | `/api/enquiry/:id` | Get a single enquiry by ID |

### Request Body — `POST /api/enquiry`

```json
{
  "name": "string (required)",
  "email": "string (required)",
  "phone": "string (required, max 10 digits)",
  "course": "string (required)",
  "message": "string (optional)"
}
```

### Database Schema — `enquiries` table

```sql
CREATE TABLE IF NOT EXISTS enquiries (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100)  NOT NULL,
  email       VARCHAR(255)  NOT NULL,
  phone       VARCHAR(10)   NOT NULL,
  course      VARCHAR(100)  NOT NULL,
  message     TEXT,
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_email (email),
  INDEX idx_course (course),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Dev Server

```bash
cd backend
npm install
npm run dev        # nodemon, starts at http://localhost:5000
```

---

## 🐳 Docker

### Frontend — Multi-Stage Build

```dockerfile
# Stage 1: Build the React app
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
```

### Backend

```dockerfile
FROM node:18-alpine
WORKDIR /backend
COPY package.json /backend
RUN npm install
COPY . .
EXPOSE 5000
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD curl -f http://localhost:5000/health || exit 1
CMD ["node", "server.js"]
```

### Build & Run Locally

```bash
# Frontend
docker build -t coding-cloud-frontend ./frontend
docker run -p 80:80 coding-cloud-frontend

# Backend
docker build -t coding-cloud-backend ./backend
docker run -p 5000:5000 --env-file ./backend/.env coding-cloud-backend
```

---

## 🔁 CI/CD — Jenkins

Two separate Jenkinsfiles, one per service, both following the same pattern:

### Pipeline Stages

```
1. Checkout SRC         (docker agent) — git pull from master
2. changeset guard      — skip if no changes in frontend/** or backend/**
3. Build Docker Image   (docker agent) — docker build -t unnadocker/<service>:${BUILD_NUMBER}
4. Push to Docker Hub   (docker agent) — docker push using 'dockerhub-credentials'
5. Update Deployment    (k8s-master agent) — kubectl set image ... -n coding-cloud
post/always:            docker image prune -a -f + cleanWs()
```

### Jenkins Agents Required

| Label | Role |
|---|---|
| `docker` | Runs Docker build and push steps |
| `k8s-master` | Runs `kubectl` rolling update |

### Required Jenkins Credentials

| ID | Type |
|---|---|
| `dockerhub-credentials` | Docker Hub username/password |

---

## ☸️ Kubernetes

All resources live in the `coding-cloud` namespace.

### Apply all manifests

```bash
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/frontend-deployment.yml
kubectl apply -f k8s/backend-deployment.yml
kubectl apply -f k8s/nginx-deployment.yml
kubectl apply -f k8s/HPA.yml
```

### Workloads Overview

| Workload | Replicas | Image | Service Type | Exposed Port |
|---|---|---|---|---|
| `frontend-deployment` | 2 | `unnadocker/coding-cloud-frontend:latest` | ClusterIP | 90 → pod:80 |
| `backend-deployment` | 2 | `unnadocker/coding-cloud-backend:latest` | ClusterIP | 5000 |
| `nginx-deployment` | 2 | `nginx:latest` | **NodePort** | 31000 → pod:80 |

### Nginx Routing (ConfigMap)

| Request Path | Forwards To |
|---|---|
| `/` | `frontend-service:90` |
| `/backend/` | `backend-service:5000/` |
| `/health` | `backend-service:5000/health` |

### Horizontal Pod Autoscaler

| HPA Name | Target Deployment | Min | Max | CPU Trigger |
|---|---|---|---|---|
| `nginx-hpa` | `nginx-deployment` | 2 | 4 | 70% |
| `frontend-hpa` | `frontend-deployment` | 2 | 5 | 70% |
| `backend-hpa` | `backend-deployment` | 2 | 8 | 70% |

---

## ☁️ Infrastructure — Terraform (AWS)

> **Provider:** `hashicorp/aws` v6.23.0 | **Region:** `ap-south-1` (Mumbai)

All infrastructure is split into three Terraform modules wired in `terraform.tf`.

### VPC (`./vpc`)

| Resource | Configuration |
|---|---|
| VPC | `10.0.0.0/16` |
| Public Subnet 1 | `10.0.0.0/19` — ap-south-1a |
| Public Subnet 2 | `10.0.32.0/19` — ap-south-1b |
| Private Subnet 1 | `10.0.64.0/19` — ap-south-1a |
| Private Subnet 2 | `10.0.96.0/19` — ap-south-1b |
| Internet Gateway | Attached to VPC |
| NAT Gateway | Deployed in Public Subnet 1 (Elastic IP) |
| Route Tables | Public → IGW; Private → NAT |
| Security Groups | ALB, Jenkins, Docker, K8s Master, K8s Worker, RDS |

### EC2 (`./ec2`)

| Instance | Type | Subnet | Public IP | Purpose |
|---|---|---|---|---|
| `Jenkins` | `t2.medium` | Public Subnet 1 | ✅ Yes | Jenkins master server |
| `Docker` | `t2.medium` | Private Subnet 1 | ❌ No | Docker build agent (Jenkins slave) |
| `K8s-Master` | `t2.medium` | Private Subnet 2 | ❌ No | Kubernetes control plane |
| `K8s-Worker` | `t2.medium` | Private Subnet 2 | ❌ No | Kubernetes worker node |

**Application Load Balancer:**
- Internet-facing, port 80 → Target Group → K8s Worker NodePort `:31000`

### RDS (`./rds`)

| Setting | Value |
|---|---|
| Engine | MySQL 8.0 |
| Instance class | `db.t3.micro` |
| Storage | 400 GB gp3 |
| Multi-AZ | No |
| Subnet | Private Subnet Group (Sub 1 + Sub 2) |
| Publicly accessible | No |

### Terraform Commands

```bash
cd Terraform
terraform init
terraform plan
terraform apply
terraform destroy   # to tear down
```

---

## 🔐 Environment Variables

### Backend (`.env`)

| Variable | Description | Example |
|---|---|---|
| `PORT` | Express server port | `5000` |
| `NODE_ENV` | Environment | `development` / `production` |
| `DB_HOST` | MySQL host (RDS endpoint) | `xxx.rds.amazonaws.com` |
| `DB_USER` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | *(use secrets manager)* |
| `DB_NAME` | MySQL database name | `enquiry` |
| `DB_PORT` | MySQL port | `3306` |
| `FRONTEND_URL` | Allowed CORS origin | `http://frontend:80` |

---

## 📝 Placeholders

Before running `terraform apply`, you **must** fill in the following blank values in the Terraform files:

### `Terraform/provider.tf` — AWS Credentials

```hcl
provider "aws" {
    region     = "ap-south-1"
    access_key = ""           # ← Paste your AWS Access Key ID here
    secret_key = ""           # ← Paste your AWS Secret Access Key here
}
```

> **How to get them:** AWS Console → IAM → Users → Your User → Security Credentials → Create Access Key

---

### `Terraform/ec2/cluster-vm.tf` — AMI IDs for K8s Nodes

```hcl
resource "aws_instance" "master" {
    ami           = ""        # ← Paste your AMI ID here (e.g. ami-0f5ee92e2d63afc18)
    instance_type = "t2.medium"
    ...
}

resource "aws_instance" "worker" {
    ami           = ""        # ← Paste your AMI ID here (e.g. ami-0f5ee92e2d63afc18)
    instance_type = "t2.medium"
    ...
}
```

### `Terraform/ec2/jenkins-docker-vm.tf` — AMI IDs for Jenkins & Docker Nodes

```hcl
resource "aws_instance" "jenkins" {
    ami           = ""        # ← Paste your AMI ID here
    instance_type = "t2.medium"
    ...
}

resource "aws_instance" "docker" {
    ami           = ""        # ← Paste your AMI ID here
    instance_type = "t2.medium"
    ...
}
```

> **How to find the right AMI ID:**  
> AWS Console → EC2 → AMI Catalog → Search for **Ubuntu 22.04** or **Amazon Linux 2023** in region `ap-south-1` → Copy the AMI ID (format: `ami-xxxxxxxxxxxxxxxxx`)

---

### Quick Placeholder Checklist

| File | Field | Status |
|---|---|---|
| `Terraform/provider.tf` | `access_key` | ☐ Fill in |
| `Terraform/provider.tf` | `secret_key` | ☐ Fill in |
| `Terraform/ec2/cluster-vm.tf` | `ami` (master) | ☐ Fill in |
| `Terraform/ec2/cluster-vm.tf` | `ami` (worker) | ☐ Fill in |
| `Terraform/ec2/jenkins-docker-vm.tf` | `ami` (jenkins) | ☐ Fill in |
| `Terraform/ec2/jenkins-docker-vm.tf` | `ami` (docker) | ☐ Fill in |

---


## 🚀 Getting Started (Local)

### Prerequisites
- Node.js 18+
- Docker
- MySQL 8.0 (local or cloud)

### 1. Clone the repo

```bash
git clone https://github.com/unnadocker-Phadtare/Shiftotech-Project.git
cd Shiftotech-Project
```

### 2. Start the backend

```bash
cd backend
cp .env.example .env        # fill in your DB credentials
npm install
npm run dev
```

### 3. Start the frontend

```bash
cd frontend
npm install
npm run dev                 # http://localhost:8080
```

### 4. Run with Docker Compose *(optional)*

```bash
# Build and start both services
docker build -t coding-cloud-frontend ./frontend
docker build -t coding-cloud-backend ./backend

docker run -d -p 5000:5000 --env-file ./backend/.env coding-cloud-backend
docker run -d -p 80:80 coding-cloud-frontend
```

---

Made with ❤️ by **unnadocker Phadtare**