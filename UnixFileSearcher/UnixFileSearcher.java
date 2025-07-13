package UnixFileSearcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UnixFileSearcher {
    public static void main(String[] args) {
        File root = new File("root", "Rahul Sharma", new BigDecimal(12123323));
        File file1 = new File("file1.txt", "Rahul Sharma", new BigDecimal(200));
        File file2 = new File("file2.pdf", "Rahul Sharma", new BigDecimal(300));

        root.addChildren(file1);
        root.addChildren(file2);

        Criteria<String> ownerCriteria = new SimplifiedCriteria<>(new EqualOperator<>(), CompareBy.OWNER, "Rahul Sharma");

        FileSearchQuery query = new FileSearchQuery(root, ownerCriteria);
        List<File> results = query.search();
        assert results.size() == 2;

        results.forEach(it -> System.out.println(it.getProperty(CompareBy.FILE_NAME)));
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

class SimplifiedCriteria<T> implements Criteria {
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

interface CompositeCriteria<T> extends Criteria {
    
}

class AndCriteria<T> implements CompositeCriteria<T> {
    List<Criteria<T>> simplifiedCriteriaList;

    

    public AndCriteria(List<Criteria<T>> simplifiedCriteriaList) {
        this.simplifiedCriteriaList = simplifiedCriteriaList;
    }

    @Override
    public boolean doesMatch(File file) {
        return simplifiedCriteriaList.stream().allMatch(it -> it.doesMatch(file));
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