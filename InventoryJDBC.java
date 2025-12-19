import java.sql.*;
import java.util.*;
//Item class//
 class Item {
    int id;
    String name;
    int quantity;
    double price;

    Item(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }


    @Override
    public String toString() {
        return "ID: " + id +
               " | Name: " + name +
               " | Qty: " + quantity +
               " | Price: " + price;
    }
}
public class InventoryJDBC {

    static Scanner sc = new Scanner(System.in);
    static Connection con;
    static List<Item> itemList = new ArrayList<>();


    public static void main(String[] args) throws Exception {
     con=DB.getConnection();
        while (true) {
            System.out.println("\n====== INVENTORY MANAGEMENT SYSTEM (JDBC) ======");
            System.out.println("1. Add Item");
            System.out.println("2. Update Item");
            System.out.println("3. Delete Item");
            System.out.println("4. View All Items");
            System.out.println("5. Search Item");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

        switch (ch) {
    case 1 -> addItem();
    case 2 -> updateItem();
    case 3 -> deleteItem();
    case 4 -> viewItems();
    case 5 -> searchItem();
    case 6 -> {
        System.out.println("Exiting...");
        return;
    }
    default -> System.out.println("Invalid choice!");
}
        }
    }

    // ADD ITEM -------------------------------------------------
    static void addItem() throws Exception {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        PreparedStatement ps = con.prepareStatement(
           "INSERT INTO items VALUES (?, ?, ?, ?)"
        );
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setInt(3, qty);
        ps.setDouble(4, price);

        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "Item Added!" : "Add Failed!");
    }

    // UPDATE ITEM ----------------------------------------------
    static void updateItem() throws Exception {
    System.out.print("Enter ID to update: ");
    int id = sc.nextInt();
    sc.nextLine(); // consume newline

    PreparedStatement ps = con.prepareStatement(
        "SELECT * FROM items WHERE id=?"
    );
    ps.setInt(1, id);

    ResultSet rs = ps.executeQuery();   // ✅ FIXED

    if (!rs.next()) {
        System.out.println("Item not found!");
        return;
    }

    System.out.print("New Name (" + rs.getString("name") + "): ");
    String name = sc.nextLine();
    if (name.isEmpty()) {
        name = rs.getString("name");
    }

    System.out.print("New Quantity (" + rs.getInt("quantity") + "): ");
    String qtyStr = sc.nextLine();
    int qty = qtyStr.isEmpty()
            ? rs.getInt("quantity")
            : Integer.parseInt(qtyStr);

    System.out.print("New Price (" + rs.getDouble("price") + "): ");
    String priceStr = sc.nextLine();
    double price = priceStr.isEmpty()
            ? rs.getDouble("price")
            : Double.parseDouble(priceStr);

    PreparedStatement ps2 = con.prepareStatement(
        "UPDATE items SET name=?, quantity=?, price=? WHERE id=?"
    );
    ps2.setString(1, name);
    ps2.setInt(2, qty);
    ps2.setDouble(3, price);
    ps2.setInt(4, id);

    int rows = ps2.executeUpdate();
    System.out.println(rows > 0 ? "Updated Successfully!" : "Update Failed!");
}


    // DELETE ITEM -----------------------------------------------
    static void deleteItem() throws Exception {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM items WHERE id=?"
        );
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "Item Deleted!" : "Item Not Found!");
    }

    // VIEW ITEMS ------------------------------------------------
   static void viewItems() throws Exception {

    itemList.clear();   // avoid duplicate entries

    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM items");

    while (rs.next()) {
        Item item = new Item(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("quantity"),
            rs.getDouble("price")
        );

        itemList.add(item);   // STORE IN COLLECTION
    }

    System.out.println("\n------ INVENTORY LIST (FROM COLLECTION) ------");

    for (Item i : itemList) {
        System.out.println(i);
    }
}


    // SEARCH ITEM ----------------------------------------------
    static void searchItem() throws Exception {
        System.out.print("Enter ID to search: ");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM items WHERE id=?"

        );
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Item Found:");
            System.out.println(
                "ID: " + rs.getInt(1) +
                " | Name: " + rs.getString(2) +
                " | Qty: " + rs.getInt(3) +
                " | Price: " + rs.getDouble(4)
            );
        } else {
            System.out.println("Item Not Found!");
        }
    }
}
