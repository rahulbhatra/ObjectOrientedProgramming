package SplitWise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

class User {
    private UUID userid;
    public UUID getUserid() {
        return userid;
    }

    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.userid = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

class Split {
    private int percentage;
    public int getPercentage() {
        return percentage;
    }
    private User user;
    public User getUser() {
        return user;
    }
    private double amountPaid = 0d;
    public double getAmountPaid() {
        return amountPaid;
    }
    private double amountOwed;
    
    public double getAmountOwed() {
        return amountOwed;
    }

    public Split(User user, double amountOwed, double amountPaid) {
        this.user = user;
        this.amountOwed = amountOwed;
        this.amountPaid = amountPaid;
    }
}

abstract class SplitStrategy {
    public boolean isSplitSumEqual(List<Split> splits, double totalAmount) {
        double splitTotalAmount = 0;
        for(Split split: splits) {
            splitTotalAmount += split.getAmountOwed();
        }
        return splitTotalAmount == totalAmount;
    }
    public abstract boolean isValidSplit(List<Split> splits, double totalAmount);
}

class EqualSplitStrategy extends SplitStrategy {

    @Override
    public boolean isValidSplit(List<Split> splits, double totalAmount) {
        return isSplitSumEqual(splits, totalAmount);
    }
    
}

class PercentageSplitStrategy extends SplitStrategy {

    boolean isCorrectPercentageSum(List<Split> splits) {
        int percentageSum = 0;
        for(Split split: splits) {
            percentageSum += split.getPercentage();
        }
        return percentageSum == 100;
    }

    @Override
    public boolean isValidSplit(List<Split> splits, double totalAmount) {
        return isSplitSumEqual(splits, totalAmount) && isCorrectPercentageSum(splits);
    }

}

class Expense {
    private UUID uuid;
    private User paidByUser;
    private List<Split> splits;

    public List<Split> getSplits() {
        return splits;
    }

    public Expense(User paidUser, List<Split> splits) {
        this.uuid = UUID.randomUUID();
        this.paidByUser = paidUser;
        this.splits = splits;
    }
}

// class Transaction {
//     private UUID formUserId;
//     private UUID toUserId;
//     private double amount;

//     public Transaction(UUID formUserId, UUID toUserId, double amount) {
//         this.formUserId = formUserId;
//         this.toUserId = toUserId;
//         this.amount = amount;
//     }    
// }

public class SplitWiseAlgorithm {

    private static Map<UUID, Double> calculateNetBalance(List<Expense> expenses) {
        Map<UUID, Double> netBalance = new HashMap<>();
        for (Expense expense: expenses) {
            for(Split split: expense.getSplits()) {
                UUID userId = split.getUser().getUserid();
                Double balance = netBalance.getOrDefault(userId, 0d);
                double splitBalance = split.getAmountOwed() + split.getAmountPaid();
                netBalance.put(split.getUser().getUserid(), balance + splitBalance);
            }
        }
        return netBalance;
    }

    private static Map<UUID, List<Map<UUID, Double>>> findMinimumTransactions(Map<UUID, Double> netBalances) {
        //Max-heap for creditors
        PriorityQueue<Map.Entry<UUID, Double>> creditors = new PriorityQueue<>(
            (a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Min-heap for debtors
        PriorityQueue<Map.Entry<UUID, Double>> debtors = new PriorityQueue<>(
            (a, b) -> Double.compare(a.getValue(), b.getValue()));
        
        // Populate queues
        for (Map.Entry<UUID, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(entry);
            } else if (entry.getValue() < 0) {
                debtors.add(entry);
            }
        }

        Map<UUID, List<Map<UUID, Double>>> result = new HashMap<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<UUID, Double> debtor = debtors.poll();
            Map.Entry<UUID, Double> creditor = creditors.poll();

            double debit = -debtor.getValue();
            double credit = creditor.getValue();
            double settledAmount = Math.min(debit, credit);

            // Record transaction: debtor pays creditor
            result.computeIfAbsent(debtor.getKey(), k -> new ArrayList<>())
                .add(Map.of(creditor.getKey(), settledAmount));

            // Update balances
            double remainingDebtor = debit - settledAmount;
            double remainingCreditor = credit - settledAmount;

            if (remainingDebtor > 0) {
                debtors.add(Map.entry(debtor.getKey(), -remainingDebtor));
            }
            if (remainingCreditor > 0) {
                creditors.add(Map.entry(creditor.getKey(), remainingCreditor));
            }
        }

        return result;

    }

    public static void main(String[] args) {
        User userA = new User("a", "a", "a");
        User userB = new User("b", "b", "b");
        User userC = new User("c", "c", "c");
        User userD = new User("d", "d", "d");

        List<Split> splitsA = Arrays.asList(
            new Split(userA, -50, 200),
            new Split(userB, -50, 0),
            new Split(userC, -50, 0),
            new Split(userD, -50, 0)
        );
        Expense expenseA = new Expense(userA, splitsA);


        List<Expense> expenses = Arrays.asList(expenseA);
        Map<UUID, Double> balanceSheet = calculateNetBalance(expenses);
        System.out.println(balanceSheet);
    }
}
