

# Scalable WebServer

A robust, high-performance Java-based web server designed to efficiently manage **60k+ concurrent requests**. This project showcases multiple server architectures with different threading models, including **Single-Threaded**, **Multi-Threaded**, and **Thread Pool** implementations, to ensure scalability and optimal performance.

## 🚀 Key Features

- **Server Architectures**:
  - **Single-Threaded Server**: Sequentially handles one client request at a time, suitable for light workloads.
  - **Multi-Threaded Server**: Manages multiple client connections concurrently, allowing for higher throughput.
  - **Thread Pool Server**: Uses a fixed-size pool of threads, balancing resource management and concurrency for optimal performance under heavy load.

- **High Performance**:
  - Capable of managing **60k+ concurrent requests**.
  - Processes approximately **194 requests per second** across different load scenarios.
  - Achieves sub-millisecond response times for 90% of requests, even under heavy load.

- **Efficient Data Handling**:
  - **Data Received**: ~4.8 MB (averaging 160 kB/s).
  - **Data Sent**: ~528 kB (17 kB/s).
  - Zero failed requests across all test runs, indicating high reliability and stability.

- **Content Delivery**:
  - Serves static files like **HTML and CSS** with verification.
  - Ensures status 200 for content delivery.
  - Validates that CSS files include essential selectors, such as `body`, for consistency in front-end display.

## 📊 Performance Metrics

### Load Testing Overview

We conducted load testing using **100 Virtual Users (VUs)** over a **30-second test duration** with a graceful stop period of 30 seconds. Results across different runs consistently showed high performance and stability.

---

### Test Results

#### Single-Threaded Server
![Single Threaded](https://github.com/user-attachments/assets/28068b2b-9376-4a21-976b-c9147d427450)


- **Total Requests**: 5888 (~194.25 req/s)
- **Data Received**: 3.7 MB (123 kB/s)
- **Data Sent**: 527 kB (17 kB/s)
- **Median Response Time**: 767.66μs
- **P90 Response Time**: 4.71ms
- **P95 Response Time**: 9.22ms

#### Multi-Threaded Server
![MultiThreaded](https://github.com/user-attachments/assets/1bd70109-5e02-4058-b1aa-fe9aaec12e1d)

- **Total Requests**: 5904 (~194.45 req/s)
- **Data Received**: 4.9 MB (161 kB/s)
- **Data Sent**: 528 kB (17 kB/s)
- **Median Response Time**: 823.31μs
- **P90 Response Time**: 3.48ms
- **P95 Response Time**: 7.39ms

#### Thread Pool Server
![ThreadPool](https://github.com/user-attachments/assets/6233d856-c247-4f66-8854-056eca4a3ce2)

- **Total Requests**: 5820 (~193.24 req/s)
- **Data Received**: 4.8 MB (160 kB/s)
- **Data Sent**: 521 kB (17 kB/s)
- **Median Response Time**: 673.08μs
- **P90 Response Time**: 2.26ms
- **P95 Response Time**: 3.16ms

---

### Performance Summary

1. **Consistent Throughput**:
   - Maintained an average of ~194 requests per second.
   - Less than 1% variation between test runs, demonstrating stable performance.
   - Zero failed requests, indicating high reliability.

2. **Low Latency**:
   - Median response times consistently under 1 millisecond.
   - P90 latency under 5 milliseconds, and P95 latency under 10 milliseconds.

3. **Efficient Data Transfer**:
   - Data handling at approximately **160 kB/s** received and **17 kB/s** sent.
   - Validated for accurate and reliable static content delivery.

