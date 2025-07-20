# Tokenized Invoice Lending Platform

A full-stack, microservices-based platform that enables tokenization of invoices for decentralized lending using Web3 and blockchain integration.

## Overview

This project demonstrates a modular architecture where each service is independently deployed and communicates via REST and RabbitMQ. Blockchain integration is done using Web3j to mint tokens representing funded invoices on-chain.

## Project Structure

```text
tokenized_invoice_lending_platform/
├── kyc_service/             # Manages SME and LP user KYC
├── invoice_service/         # Handles invoice creation and validation
├── wallet_service/          # Generates and manages blockchain wallet addresses
├── funding_service/         # LP funds invoices, token minted, on-chain receipt stored
```

## Tech Stack

- **Java 17**, 
- **Spring Boot 3**
- **PostgreSQL** (used in all services — no H2)
- **RabbitMQ** (for asynchronous messaging)
- **Web3j** (Blockchain Integration - smart contract interaction)
- **Ganache CLI** (local Ethereum simulation)
- **Docker & Docker Compose** (multi-service orchestration)
- **Swagger/OpenAPI** (REST API testing)

## Features
- SME onboarding with wallet generation
- Invoice creation & validation
- Tokenization and funding of invoice
- Blockchain-based minting of invoice tokens
- Retry mechanism on mint failure
- Token balance retrieval from chain

## Inter-service Communication
- **REST**: For synchronous calls like fetching user/wallet/invoice info
- **RabbitMQ**: For async funding completion notification

## Running Locally

### 1. Clone the repo

```bash
   git clone https://github.com/your-username/tokenized_invoice_lending_platform.git
   cd tokenized_invoice_lending_platform
```

### 2. Start PostgreSQL, RabbitMQ & Ganache

```bash
   docker-compose -f docker-compose.yml up -d
```
- **PostgreSQL**: localhost:5432 (user: postgres, pass: postgres)
- **RabbitMQ UI**: http://localhost:15672 (guest/guest)
- **Ganache (Ethereum)**: http://localhost:8545

### 3. Build & Run Services

Each service is a Spring Boot app. You can start them via:

```bash
   cd funding_service
   ./mvnw spring-boot:run
```
Repeat for invoice_service, kyc_service, and wallet_service.

## Blockchain Integration

We use Web3j to interact with the smart contract deployed on Ganache. 
- Solidity Smart Contracts (InvoiceToken.sol)
- Ganache or Testnet-compatible node 
- Web3j for Java ↔ Blockchain communication

### Smart Contract Flow

1.	Deploy your contract using Remix or Truffle on Ganache.
2.	Generate contract wrappers using web3j:
```bash
   web3j generate solidity -a InvoiceToken.abi -b InvoiceToken.bin -o src/main/java -p io.github.deepaganesh.contracts
```

Contract artifacts (.abi and .bin) are placed under: **funding_service/src/main/resources/contracts/InvoiceToken.**

## Swagger Endpoints

Run services and navigate to:

- KYC: http://localhost:8085/swagger-ui/index.html
- Invoice: http://localhost:8086/swagger-ui/index.html
- Wallet: http://localhost:8087/swagger-ui/index.html
- Funding: http://localhost:8088/swagger-ui/index.html
