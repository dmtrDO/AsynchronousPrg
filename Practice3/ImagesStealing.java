
import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public class ImagesStealing {
    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg"};

    private static class ImagesFinder extends RecursiveTask<ArrayList<File>> {
        private final File directory;

        public ImagesFinder(File directory) {
            this.directory = directory;
        }

        @Override
        protected ArrayList<File> compute() {
            ArrayList<File> result = new ArrayList<File>();
            ArrayList<ImagesFinder> subTasks = new ArrayList<>();
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        ImagesFinder task = new ImagesFinder(file);
                        task.fork();
                        subTasks.add(task);
                    } else if (isImage(file)) {
                        result.add(file);
                    }
                }
                for (ImagesFinder task : subTasks) {
                    result.addAll(task.join());
                }
            }
            return result;
        }

        private boolean isImage(File file) {
            String name = file.getName().toLowerCase();
            for (String extension : IMAGE_EXTENSIONS) {
                if (name.endsWith(extension)) return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter directory path to search images: ");
        String dirPath = sc.nextLine();
        sc.close();
        File dir = new File(dirPath);

        if (!dir.isDirectory()) {
            System.out.println("Provided path is not a directory");
            return;
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ArrayList<File> images = forkJoinPool.invoke(new ImagesFinder(dir));
  
        System.out.println("Number of images found: " + images.size());
        
        if (!images.isEmpty()) {
            File lastImage = images.get(images.size() - 1);
            System.out.println("Opening lst image: " + lastImage.getAbsolutePath());
            try {
                java.awt.Desktop.getDesktop().open(lastImage);
            } catch (Exception e) {
                System.out.println("Failed to open image: " + e.getMessage());
            }
        }
    }

}
