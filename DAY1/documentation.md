DOCUMENTATION FOR COFFEE VENDING MACHINE LLD DESIGN

STEP 1: IDENTIFYING REQUIREMENTS:

I. USER FUNCTIONAL REQUIREMENTS:

    - User selects one or many canned coffees they want
    - User can view the current cart status before checkout
    - User confirms that the cart can be taken further for billing by the system
    - User selects from the multiple payment services to pay
    - The items are dispatched for user to pick up from the pickup section

II. TECHNICIAN/OPERATOR FUNCTIONAL REQUIREMENTS:

    - Technician can refill or restock columns that are empty or near empty.
    - Technician can update the prices for the items in a specific column.

III. VENDING MACHINE'S FUNCTIONAL REQUIREMENTS:

    - displaying the inventory for the user to select their coffee preference
    - The system will process the selection of items for billing once the user confirms it
    - The system will provide the payment service for the user
    - After payment confirmation, the items are dispatched according to their columns
    - After dispatch, the system inventory gets updated. Empty columns can't be selected.



STEP 2: IDENTIFYING ENTITIES:

I. ENTITY CLASSES:

1. Item class:
    - Contains item details like name and price
    - Used to give info about item price and name
    ATTRIBUTES:
        - item name
        - item price
    METHODS:
        - getItemName()
        - setItemName()
        - getItemPrice()
        - setItemPrice()

2. Column class:
    - Contains the details like column capacity, column ID, current stock and items
    - Used to get info about column ID and if column is empty or not.
    ATTRIBUTES:
        - column ID
        - column capacity
        - current stock
        - item
    METHODS:
        - isEmpty()
        - dispenseOne()
        - getStockCount()
        - restock(n)

3. Inventory class:
    - Contains details about the entire vending machine inventory (columns)
    - Used to get info about available columns, get stock count
    ATTRIBUTES:
        - Inventory ID
        - list of columns
    METHODS:
        - isEmpty()
        - getStockCount()

4. Cart class:
    - Holds items with quantities
    - Calculates total
    - Responsible for updating quantity
    ATTRIBUTES:
        - Cart ID
        - Map of items with quantity
        - Cart Amount
    METHODS:
        - addToCart(item)
        - removeFromCart(item)
        - displayCart()
        - updateCart(item, quantity)

5. Technician class:
    - Contains technician details
    - Used for getting info of Technician
    ATTRIBUTES:
        - Technician ID
        - Technician name
    METHODS:
        - getTechnicianName()
        - setTechnicianName()
        - getTechnicianID()
        - setTechnicianID()
        - restockMachine()
        
II. SERVICE CLASSES:

1. DisplayService class
    - Display Items in columns with ColumnID, Item name and Item price
2. PaymentService class
    - Provide choices between multiple payment services (gpay, Netbanking)
3. DispenseService class
    - Dispense items as per user order and updates inventory after dispense.


