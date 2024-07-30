package cs2110;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.util.Scanner;


public class CsvJoin {

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException {
        Reader in;
        Seq<Seq<String>> list = new LinkedSeq<>();
        Seq<String> element;
        Scanner sc;
        String[] x;
        int len = 0;
        try {
            in = new FileReader(file);
            sc = new Scanner(in);
        } catch (IOException e) {
            throw new IOException("File: " + file + " was unable to be read");
        }
        while (sc.hasNextLine()) {
            element = new LinkedSeq<>();
            x = (sc.nextLine().split(",", -1));
            len = x.length;
            for (int i = 0; i < len; i++) {
                element.append(x[i]);
            }
            list.append(element);
        }
        return list;
    }

    /**
     * Asserts that the preconditions for join are satisfied Requires that list represents a
     * rectangular table that has more than 1 column.
     */

    public static void testValidity(Seq<Seq<String>> list) {
        int count = 0;
        int size = list.get(0).size();
        for (Seq<String> row : list) {
            if (row.size() > size) {
                size = row.size();
            }
            count += 1;
        }
        assert size == list.get(0).size() : "Table is not rectangular";
        assert count > 1 : "Table does not have more than 1 column";
    }


    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) {
        testValidity(left);
        testValidity(right);
        Seq<Seq<String>> list = new LinkedSeq<>();
        Seq<String> element;
        int max = 0;
        for (Seq<String> leftRow : left) {
            element = new LinkedSeq<>();
            for (int i = 0; i < leftRow.size(); i++) {
                element.append(leftRow.get(i));
            }

            for (Seq<String> rightRow : right) {
                if (rightRow.get(0).equals(leftRow.get(0))) {
                    for (int i = 1; i < rightRow.size(); i++) {
                        element.append(rightRow.get(i));
                    }
                }
            }
            list.append(element);

            for (Seq<String> x : list) {
                if (x.size() > max) {
                    max = x.size();
                }
            }

            for (Seq<String> x : list) {
                if (x.size() < max) {
                    while (x.size() < max) {
                        x.append("");
                    }
                }
            }

        }
        testValidity(list);
        return list;
    }

    /**
     * Requires two arguements which represent csv file names Prints out the left outer joined
     * result of the two files Catches various exceptions that could occur while executing
     */

    public static void main(String[] args) throws IOException {
        try {
            String leftFile = args[0];
            String rightFile = args[1];
            String csv = "";
            Seq<Seq<String>> leftList = csvToList(leftFile);
            Seq<Seq<String>> rightList = csvToList(rightFile);
            Seq<Seq<String>> joined = join(leftList, rightList);
            for (Seq<String> row : joined) {
                for (int i = 0; i < row.size(); i++) {
                    csv += row.get(i);
                    if (i < row.size() - 1) {
                        csv += ",";
                    }
                }
                csv += "\n";
            }

            System.out.println(csv);

        } catch (FileNotFoundException e) {
            System.err.println("Error: missing.csv No such file or directory" + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: An IO error occured" + e.getMessage());
            System.exit(1);
        } catch (AssertionError e) {
            System.err.println("Error: The join methods preconditions are not satisfied");
            System.exit(1);
        }

    }

}


