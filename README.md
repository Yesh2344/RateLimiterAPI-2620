# RateLimiterAPI

[![Build Status](https://github.com/yourusername/RateLimiterAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/yourusername/RateLimiterAPI/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## Overview

RateLimiterAPI is a production‑ready Spring Boot application that demonstrates a robust **REST API rate limiting** solution. 
It uses **Bucket4j** for token‑bucket based throttling, applies the limit per client IP, and provides clean error handling, logging, and comprehensive tests.

## Features

- Configurable request limits (requests per minute) via `application.yml`
- Global exception handling returning HTTP **429 Too Many Requests**
- Detailed request logging with SLF4J
- Integration tests with MockMvc
- Docker‑compatible configuration (`.env.example`)

## Prerequisites

- JDK 17 or newer
- Gradle 8.x
- Docker (optional, for containerised deployment)

## Installation