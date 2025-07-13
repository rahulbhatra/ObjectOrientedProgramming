package UnixFileSearcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UnixFileSearcher {
    public static void main(String[] args) {
        try {
            testByOwner();
            testBySize();
            testCompositeCriteria();
            System.out.println("✅ All tests passed.");
        } catch(AssertionError error) {
            System.err.println("Assertion error found");
            error.printStackTrace();
        }
    }

    static void testByOwner() {
        File root = new File("root", "admin", new BigDecimal(1000));
        File file1 = new File("file1.txt", "alice", new BigDecimal(200));
        File file2 = new File("file2.txt", "bob", new BigDecimal(300));
        root.addChildren(file1);
        root.addChildren(file2);

        Criteria<String> criteria = new SimplifiedCriteria<>(new EqualOperator<>(), CompareBy.OWNER, "alice");
        FileSearchQuery query = new FileSearchQuery(root, criteria);

        List<File> result = query.search();
        assert result.size() == 1 : "Expected 1 file";
        assert result.get(0).getProperty(CompareBy.OWNER).equals("alice");
    }

    static void testBySize() {
        File root = new File("root", "user", new BigDecimal(999));
        File file1 = new File("bigfile.txt", "user", new BigDecimal(5000));
        File file2 = new File("smallfile.txt", "user", new BigDecimal(100));
        root.addChildren(file1);
        root.addChildren(file2);

        Criteria<BigDecimal> sizeCriteria = new SimplifiedCriteria<>(new GreaterThanOperator<>(), CompareBy.SIZE, new BigDecimal(1000));
        FileSearchQuery query = new FileSearchQuery(root, sizeCriteria);

        List<File> result = query.search();
        assert result.size() == 1 : "Expected 1 file";
        assert result.get(0).getProperty(CompareBy.FILE_NAME).equals("bigfile.txt");
    }

    static void testCompositeCriteria() {
        File root = new File("root", "admin", new BigDecimal(999));
        File file1 = new File("doc1.pdf", "admin", new BigDecimal(300));
        File file2 = new File("doc2.pdf", "admin", new BigDecimal(150));
        root.addChildren(file1);
        root.addChildren(file2);

        Criteria<BigDecimal> sizeCriteria = new SimplifiedCriteria<>(new GreaterThanOperator<>(), CompareBy.SIZE, new BigDecimal(200));
        Criteria<String> extCriteria = new SimplifiedCriteria<>(new EqualOperator<>(), CompareBy.EXTENSION, "pdf");

        List<Criteria> conditions = List.of(sizeCriteria, extCriteria);
        Criteria andCriteria = new AndCriteria(conditions);

        FileSearchQuery query = new FileSearchQuery(root, andCriteria);
        List<File> result = query.search();

        assert result.size() == 1 : "Expected 1 matching file";
        assert result.get(0).getProperty(CompareBy.FILE_NAME).equals("doc1.pdf");
    }
}

class File {
    private Set<File> children;

    public void addChildren(File file) {
        this.children.add(file);
    }

    public Set<File> getChildren() {
        return Set.copyOf(this.children);
    }
    private String fileName;
    private String owner;
    private BigDecimal size;
    private String extension;
    private Boolean isDirectory;

    public File(String fileName, String owner, BigDecimal size) {
        this.fileName = fileName;
        this.owner = owner;
        this.size = size;
        this.isDirectory = !fileName.contains(".");
        this.extension = this.isDirectory ? "" : fileName.substring(fileName.lastIndexOf('.') + 1);
        this.children = new HashSet<>();
    }

    public Object getProperty(CompareBy compareBy) {
        switch (compareBy) {
            case FILE_NAME:
                return this.fileName;
            case SIZE:
                return this.size;
            case OWNER:
                return this.owner;
            case EXTENSION:
                return this.extension;
            default:
                throw new IllegalStateException("Compare by: " + compareBy.toString() + "is not supported");
        }
    }
}

enum CompareBy {
    FILE_NAME,
    SIZE,
    OWNER,
    EXTENSION
}

class FileSearchQuery {
    private final File file;
    private final Criteria criteria;

    public FileSearchQuery(File file, Criteria criteria) {
        this.file = file;
        this.criteria = criteria;
    }

    private Set<File> getFileDFS(File file) {
        Set<File> files = file.getChildren();
        Set<File> allFiles = new HashSet<>();
        allFiles.add(file);
        for(File children: files) {
            allFiles.addAll(getFileDFS(children));
        }
        return allFiles;
    }

    public List<File> search() {
        Set<File> files = getFileDFS(this.file);
        List<File> resultFiles = new ArrayList<>();
        for(File file: files) {
            if (criteria.doesMatch(file)) {
                resultFiles.add(file);
            }
        }
        return resultFiles;
    }
}

interface Criteria<T> {
    boolean doesMatch(File file);
}

interface ComparisonOperator<T> {
    boolean compare(T first, T second);
}

class EqualOperator<T> implements ComparisonOperator<T> {
    public boolean compare(T first, T second) {
        return Objects.equals(first, second);
    }
}

class LessThanOperator<T extends Comparable<T>> implements ComparisonOperator<T> {
    @Override
    public boolean compare(T first, T second) {
       return first.compareTo(second) < 0;
    }
}

class GreaterThanOperator<T extends Comparable<T>> implements ComparisonOperator<T> {
    @Override
    public boolean compare(T first, T second) {
       return first.compareTo(second) > 0;
    }
}

class SimplifiedCriteria<T> implements Criteria<T> {
    private ComparisonOperator<T> comparisonOperator;
    private CompareBy compareBy;
    private T compareByValue;

    

    public SimplifiedCriteria(ComparisonOperator<T> comparisonOperator, CompareBy compareBy, T compareByValue) {
        this.comparisonOperator = comparisonOperator;
        this.compareBy = compareBy;
        this.compareByValue = compareByValue;
    }



    @Override
    public boolean doesMatch(File file) {
        Object actualValue = file.getProperty(compareBy);
        return comparisonOperator.compare((T) actualValue, compareByValue);
    }

}

interface CompositeCriteria<T> extends Criteria<T> {
    
}

class AndCriteria<T> implements CompositeCriteria<T> {
    List<Criteria<T>> criteriaList;

    public AndCriteria(List<Criteria<T>> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public boolean doesMatch(File file) {
        return criteriaList.stream().allMatch(it -> it.doesMatch(file));
    }
}

class OrCriteria<T> implements CompositeCriteria<T> {
    List<Criteria<T>> simplifiedCriteriaList;

    public OrCriteria(List<Criteria<T>> simplifiedCriteriaList) {
        this.simplifiedCriteriaList = simplifiedCriteriaList;
    }

    @Override
    public boolean doesMatch(File file) {
        return simplifiedCriteriaList.stream().anyMatch(it -> it.doesMatch(file));
    }
}

class NotCriteria<T> implements CompositeCriteria<T> {
    SimplifiedCriteria<T> simplifiedCriteria;

    public NotCriteria(SimplifiedCriteria<T> simplifiedCriteria) {
        this.simplifiedCriteria = simplifiedCriteria;
    }

    @Override
    public boolean doesMatch(File file) {
        return !simplifiedCriteria.doesMatch(file);
    }
}