import java.io.*;
import java.util.Scanner;


public class Terminal {
    static Parser parser= new Parser();
    public static File currentDirectory = new File(System.getProperty("user.home"));
    public static File currentpath = new File(System.getProperty("user.dir"));

    public static void echo(String input) {
        System.out.println(input);
    }
    public static String pwd(){
        String path = String.valueOf(currentpath);
        return path;
    }

    public static void ls(){
        File newFile = new File(pwd());
        String[] fileList = newFile.list();
        for(String str : fileList) {
            System.out.println(str);
        }
        return;
    }
    //print in reverse  order
    public static void ls_r(){
        File newFile = new File(pwd());
        String[] fileList = newFile.list();
        int num = (fileList.length)-1;
        String[] ls = fileList;
        while(num>=0){
            System.out.println(ls[num]);
            num--;
        }
        return;
    }
    public static void mkdir(String[] fileName) throws IOException {
        for (int i = 0; i < fileName.length; i++) {
            File dir;
            if (fileName.length == 0) {
                System.out.println("you didn't enter arguments");
            } else {
                if (fileName[i].contains("\\")) {
                    dir = new File(fileName[i]);
                    if (!fileName[i].contains(":")) {
                        File parentFolder = new File(dir.getParent());
                        File b = new File(parentFolder, fileName[i]);
                        String fullPath = b.getCanonicalPath();
                        dir= new File(fullPath);
                    }
                } else {
                    dir = new File(currentpath + "\\" + fileName[i]);
                }

                    if (!dir.exists()) {
                        boolean result = dir.mkdir(); //Create the directory
                        if (result) {
                            System.out.println("directory " + (i + 1) + " created ");
                        }
                    } else {
                        System.out.println("The directory " + (i + 1) + " already exists");
                    }
                }
            }
        return;

    }



    public static File makeAbsolute(String sourcePath){
        File file = new File(sourcePath);
        if(!file.isAbsolute()) {
            file = new File(currentDirectory.getAbsolutePath(), sourcePath);
        }
        return file.getAbsoluteFile();

    }
    public static void cd(){
        try {
            if(parser.args.length==0) {
                File newfile = new File((System.getProperty("user.home")));
                currentpath=newfile;
            } else if (parser.args[0].equals("..")){
                String parent = currentpath.getParent();
                File newFile = new File(parent);
                currentpath = newFile;
            } else {
                File newFile;
                File dir = new File(parser.args[0]);
                if (!parser.args[0].contains(":")) {
                    File parentFolder = new File(dir.getParent());
                    File b = new File(parentFolder, parser.args[0]);
                    String fullPath = b.getCanonicalPath();
                    dir = new File(fullPath);
                    newFile = dir;
                } else
                    newFile = makeAbsolute(parser.args[0]);
                if (!newFile.exists()) {
                    System.out.println("Invalid directory");
                }
                if (newFile.isFile()) {
                    throw new IOException("Can't cd into file");
                } else currentpath = newFile.getAbsoluteFile();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return;
    }
    // ...
    public static void rmdir(String filename) {
        try {
            File dir;
            if (filename.contains("\\")) {
                dir = new File(filename);
                if (!filename.contains(":")) {
                    File parentFolder = new File(dir.getParent());
                    File b = new File(parentFolder, filename);
                    String fullPath = b.getCanonicalPath();
                    dir= new File(fullPath);
                }
            } else
                dir = makeAbsolute(parser.args[0]);
            // delete the directory
            boolean result = dir.delete();
            if (result) {
                System.out.println("Directory Deleted");
            } else {
                System.out.println("Directory Not Found or Not Empty");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return;
    }


    public static void deleteEmptyDirectories(File folder) {


        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                deleteEmptyDirectories(fileEntry);
                if(fileEntry.listFiles().length == 0){
                    fileEntry.delete();
                }
            }
        }

        return;
    }

    private static void cp(String src, String dest) throws IOException {
        FileReader fin;
        FileWriter fout;

        if (src.contains("\\"))
            fin = new FileReader(src);
        else
            fin = new FileReader(currentpath + "\\" + src);
        if (dest.contains("\\"))
            fout = new FileWriter(dest, false);
        else
            fout = new FileWriter(currentpath + "\\" + dest, false);


        int c;
        while ((c = fin.read()) != -1) {
            fout.write(c);
        }
        System.out.println("Files Copied");
        fin.close();
        fout.close();

        return;

    }

    public static void cpr(String src, String dest) throws IOException {
        File srcDir;
        File destDir;

        if(src.contains("\\"))
            srcDir=new File(src);
        else
            srcDir= new File(currentpath+"\\"+src);
        if(dest.contains("\\"))
            destDir= new File(dest);
        else
            destDir=new File(currentpath+"\\"+dest);

        // Make sure srcDir exists
        if (!srcDir.exists()) {
            System.out.println("Directory does not exist.");
        } else {
            Terminal fileDemo = new Terminal();
            for(File file: destDir.listFiles())
                if (!file.isDirectory())
                    file.delete();
            fileDemo.copydir(srcDir, destDir);
            System.out.println("Copied successfully.");
        }
        return;
    }

    public static void copydir(File src, File dest) throws IOException
    {
        if (src.isDirectory())
        {
            // if directory not exists, create it
            if (!dest.exists())
            {
                dest.mkdir();
                System.out.println("Directory copied from " + src + "  to "
                        + dest);
            }
            // list all the directory contents
            String files[] = src.list();

            for (String fileName : files)
            {
                // construct the src and dest file structure
                File srcFile = new File(src, fileName);
                File destFile = new File(dest, fileName);
                // recursive copy
                copydir(srcFile, destFile);
            }
        }
        else
        {
            // If file, then copy it
            fileCopy(src, dest);
        }
        return;
    }

    public static void fileCopy(File src, File dest)
            throws FileNotFoundException, IOException
    {
        InputStream in = null;
        OutputStream out = null;

        try
        {
            // If file, then copy it
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            // Copy the file content in bytes
            while ((length = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, length);
            }
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }
        System.out.println("File copied from " + src + " to " + dest);

    }



    public static void rm(String filename)
    {
        String p= String.valueOf(currentpath);
        if(p.charAt(p.length()-2)==':')
        {
            p=p+filename;
        }
        else
        {
            p=p+'/'+filename;
        }
        File file = new File(p);
        if(file.delete())
        {
            System.out.println("File Deleted Successfully");
        }
        else
        {
            System.out.println("File Cannot be Deleted !");
        }
        return;
    }



    public static void cat(String[] filename) throws IOException {
        for(int i=0;i<parser.args.length;i++) {
            File fileNamee;
            if (filename[i].contains("\\")) {
                fileNamee = new File(filename[i]);
            } else {
                fileNamee = new File(currentpath + "\\" + filename[i]);

            }
            if (fileNamee.exists() == true) {
                FileInputStream inFile = new FileInputStream(fileNamee);
                int fileLength = (int) fileNamee.length();
                byte Bytes[] = new byte[fileLength];

                inFile.read(Bytes);

                String file1 = new String(Bytes);
                System.out.println("File content is:" + file1);
                //close file
                inFile.close();
            }
            else
                System.out.println("file not exist");
        }
    }



    public static void touch(String name) throws IOException {
        File file; //initialize File object and passing path as argument
        boolean result;
        try
        {
            if (!parser.args[0].contains(":")) {
                file=new File(parser.args[0]);
                File parentFolder = new File(file.getParent());
                File b = new File(parentFolder, parser.args[0]);
                String fullPath = b.getCanonicalPath();
                file = new File(fullPath);
            }
            else {
                file = new File(name);
            }
            result = file.createNewFile();  //creates a new file
            if(result)      // test if successfully created a new file
            {
                System.out.println("File Created  Successfully in "+file.getCanonicalPath()); //returns the path string
            }
            else
            {
                System.out.println("File already exist at location: "+file.getCanonicalPath());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();    //prints exception if any
        }
        return;
    }
    public static void exit() {
        System.exit(0);
    }


    public static void chooseCommandAction() throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("enter your command: ");
        String input = sc.nextLine();
        parser.parse(input);
        String command=parser.commandName;
        if(command.equals("echo")) {
            echo(parser.args[0]);
        }
        else if(command.equals("pwd")) {
            System.out.println(pwd());
        }
        else if (command.equals("ls")) {
            if(parser.args.length==0) ls();
            else ls_r();
        }

        else if(command.equals("mkdir")) {
            mkdir(parser.args);
        }
        else if(command.equals("cd")) {
            cd();
        }
        else if (command.equals("rmdir")) {
            if(parser.args[0].equals("*"))
            {
                deleteEmptyDirectories(currentpath);
            }
            else  rmdir(parser.getArgs()[0]);

        }
        else if (command.equals("cp")) {
            if(parser.args[0].equals("-r"))
                cpr(parser.getArgs()[1], parser.getArgs()[2]);
            else
            {
                cp(parser.getArgs()[0], parser.getArgs()[1]);
            }

        }

        else if (command.equals("rm")) {
            rm(parser.args[0]);
        }

        else if (command.equals("cat")) {
            cat(parser.args);

        }

        else if (parser.getCommandName().equals("touch")) {
            touch(parser.args[0]);

        } else if (command.equals("exit")) {
            exit();
        }
    }

    public static void main(String[] args) throws IOException {

        while (true) {
            Terminal.chooseCommandAction();


        }
    }

}
