# Atinka Meds Pharmacy Inventory Management System

## Project Overview

This is a custom-built Pharmacy Inventory Management System designed for Atinka Meds, a medium-sized community pharmacy located in Adenta, Accra. The system is built using core Java and custom data structures, designed for offline-first performance to handle unreliable internet connectivity.

## Team Information

**Group 37**

- **Project**: Pharmacy Inventory Management System
- **Client**: Atinka Meds (Adenta, Accra)
- **Technology**: Java with Custom Data Structures
- **Development Approach**: Offline-first, No external libraries

## System Features

### 1. Drug Management

- ✅ Add, remove, update and list drugs
- ✅ Track drug code, name, supplier(s), expiration date, price, stock level
- ✅ Multiple suppliers per drug support
- ✅ Automatic stock level monitoring

### 2. Search & Sort Capabilities

- ✅ Binary search for drug by code (O(1) with HashMap)
- ✅ Linear search for drug by name (O(n))
- ✅ Search by supplier
- ✅ Sort drugs alphabetically (Merge Sort - O(n log n))
- ✅ Sort drugs by price (Quick Sort - O(n log n))
- ✅ Sort drugs by stock level (Insertion Sort - O(n²))

### 3. Stock Monitoring

- ✅ Real-time stock level tracking
- ✅ Low stock alerts with configurable thresholds
- ✅ Expired drug identification
- ✅ Stock reordering suggestions using Min-Heap

### 4. Data Persistence

- ✅ File-based storage (CSV format)
- ✅ Automatic data backup functionality
- ✅ Data integrity validation

## Technical Architecture

### Data Structures Used

1. **MyHashMap<K, V>** - Fast drug lookups (O(1) average)

   - Separate chaining for collision resolution
   - Dynamic resizing with load factor 0.75
   - Used for: Drug storage and retrieval

2. **MyLinkedList<T>** - Transaction history management

   - Doubly-linked implementation
   - Efficient insertion/deletion
   - Used for: Purchase history tracking

3. **MyQueue<T>** - Purchase processing

   - Circular array implementation
   - FIFO operation for transaction processing
   - Used for: Order processing pipeline

4. **MyStack<T>** - Sales log management

   - Dynamic array with auto-resizing
   - LIFO operation for recent sales tracking
   - Used for: Sales history and undo operations

5. **MyMinHeap<T>** - Stock priority management

   - Array-based binary heap
   - Efficient minimum extraction
   - Used for: Low stock drug prioritization

6. **MyTree<T>** - Binary Search Tree for suppliers
   - Balanced tree for sorted data
   - Efficient search and traversal
   - Used for: Supplier organization

### Algorithms Implemented

1. **Sorting Algorithms**

   - Merge Sort: O(n log n) - Drug name sorting
   - Quick Sort: O(n log n) average - Price sorting
   - Insertion Sort: O(n²) - Small dataset sorting
   - Selection Sort: O(n²) - Alternative sorting method
   - Bubble Sort: O(n²) - Educational implementation

2. **Search Algorithms**
   - Binary Search: O(log n) - Sorted array searching
   - Linear Search: O(n) - Unsorted data searching
   - Hash-based Search: O(1) average - Direct key lookup

## File Structure

```
Group37/
├── src/
│   ├── app/                    # Main application entry point
│   │   └── Main.java          # Menu-driven interface
│   ├── models/                # Data models
│   │   ├── Drug.java
│   │   ├── Supplier.java
│   │   ├── Customer.java
│   │   └── Transaction.java
│   ├── structures/            # Custom data structures
│   │   ├── MyLinkedList.java
│   │   ├── MyQueue.java
│   │   ├── MyStack.java
│   │   ├── MyMinHeap.java
│   │   ├── MyHashMap.java
│   │   └── MyTree.java
│   ├── services/              # Business logic
│   │   ├── DrugService.java
│   │   ├── StockService.java
│   │   ├── PurchaseService.java
│   │   └── SalesService.java
│   ├── storage/               # Data persistence
│   │   ├── DrugStore.java
│   │   ├── CustomerStore.java
│   │   └── TransactionStore.java
│   └── utils/                 # Utility classes
│       ├── FileUtils.java
│       ├── InputUtils.java
│       └── SortUtils.java
├── data/                      # Data files
│   ├── drugs.txt
│   ├── suppliers.txt
│   ├── customers.txt
│   ├── transactions.txt
│   └── saleslog.txt
├── reports/                   # Generated reports
│   └── algorithm_analysis.txt
└── README.md
```

## How to Run

### Prerequisites

- Java 8 or higher
- Terminal/Command prompt access

### Compilation

```bash
javac -cp . src/**/*.java -d build
```

### Execution

```bash
java -cp build app.Main
```

## Usage Guide

### Main Menu Options

1. **Drug Management**

   - Add new drugs with complete information
   - Update existing drug details
   - Remove drugs from inventory
   - View detailed drug information
   - List all drugs with status

2. **Search & Sort Drugs**

   - Search by drug code (instant lookup)
   - Search by drug name (partial matches)
   - Search by supplier
   - Sort drugs by various criteria

3. **Stock Management**

   - Update stock levels
   - Add stock (restocking)
   - View low stock alerts
   - Monitor expired drugs
   - Generate stock reports

4. **System Information**
   - View system statistics
   - Data structure information
   - Algorithm performance metrics

### Sample Data Format

**drugs.txt**:

```csv
# Drug Code,Name,Price,Stock Level,Expiration Date,Min Threshold,Suppliers
P001,Paracetamol 500mg,2.50,150,2025-12-31,20,PharmaCorp,MediSupply
A001,Amoxicillin 250mg,5.75,45,2025-06-15,15,PharmaCorp
```

## Algorithm Analysis

### Time Complexity Analysis

| Operation           | Data Structure     | Best Case  | Average Case | Worst Case |
| ------------------- | ------------------ | ---------- | ------------ | ---------- |
| Drug Lookup         | HashMap            | O(1)       | O(1)         | O(n)       |
| Add Drug            | HashMap            | O(1)       | O(1)         | O(n)       |
| Sort by Name        | Array + Merge Sort | O(n log n) | O(n log n)   | O(n log n) |
| Sort by Price       | Array + Quick Sort | O(n log n) | O(n log n)   | O(n²)      |
| Stock Priority      | Min Heap           | O(log n)   | O(log n)     | O(log n)   |
| Transaction History | LinkedList         | O(1)       | O(1)         | O(1)       |

### Space Complexity Analysis

| Data Structure | Space Complexity | Justification                 |
| -------------- | ---------------- | ----------------------------- |
| HashMap        | O(n)             | Stores n key-value pairs      |
| LinkedList     | O(n)             | Stores n nodes with data      |
| Min Heap       | O(n)             | Array-based storage           |
| Queue          | O(n)             | Circular array implementation |
| Stack          | O(n)             | Dynamic array with growth     |

## Design Decisions & Trade-offs

### 1. HashMap vs TreeMap for Drug Storage

**Decision**: Used custom HashMap
**Reasoning**:

- O(1) average lookup time vs O(log n) for TreeMap
- Most operations involve direct drug code lookup
- Sorting is done separately when needed

### 2. Array-based vs Node-based Heap

**Decision**: Used array-based Min Heap
**Reasoning**:

- Better cache locality
- No pointer overhead
- Simpler implementation for educational purposes

### 3. File-based vs In-memory Storage

**Decision**: File-based with in-memory caching
**Reasoning**:

- Persistence across application restarts
- Backup and recovery capabilities
- Offline-first requirement satisfaction

### 4. Custom Data Structures vs Java Collections

**Decision**: Custom implementations
**Reasoning**:

- Educational value and demonstration of understanding
- Full control over implementation details
- No external dependencies (project requirement)

## Performance Characteristics

### Strengths

- **Fast Lookups**: O(1) drug retrieval by code
- **Efficient Sorting**: Multiple optimized sorting algorithms
- **Memory Efficient**: Custom data structures optimized for use case
- **Scalable**: Designed to handle growing inventory

### Limitations

- **File I/O Overhead**: Disk operations for persistence
- **Memory Usage**: All data loaded in memory
- **Concurrency**: Single-threaded design

## Future Enhancements

1. **Transaction Processing**: Complete purchase/sales workflow
2. **Customer Management**: Full customer relationship management
3. **Supplier Integration**: Automated reordering system
4. **Reporting System**: Comprehensive analytics and reports
5. **Data Export**: Integration with external systems
6. **Multi-threading**: Concurrent access support

## Testing

The system has been tested with:

- Sample drug inventory (5 items)
- Various search and sort operations
- Stock level monitoring
- File persistence operations
- Error handling scenarios

## Conclusion

This Pharmacy Inventory Management System demonstrates effective use of custom data structures and algorithms to solve real-world business problems. The offline-first design ensures reliable operation in areas with unreliable internet connectivity, making it suitable for Atinka Meds' operational environment in Adenta, Accra.

The implementation showcases understanding of:

- Data structure selection and trade-offs
- Algorithm complexity analysis
- Software design principles
- File-based persistence
- User interface design
- Error handling and validation

---

**Project Completed**: July 2025  
**Technology Stack**: Java 8+, Custom Data Structures, File-based Storage  
**Design Pattern**: MVC (Model-View-Controller)  
**Development Approach**: Offline-first, Educational, Production-ready
