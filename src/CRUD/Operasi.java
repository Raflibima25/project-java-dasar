package CRUD;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {

    public static void updateData() throws IOException {
        // kita ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilkanData();

        // ambil user input / pilihan data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan nomer buku yang ingin diupdate: ");
        int  updateNum = terminalInput.nextInt();

        // tampilkan data yang ingin diupdate
        String data = bufferInput.readLine();
        int entryCounts = 0;

        while (data != null) {
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data,",");

            // tampilkan entrycounts == updateNum
            if (updateNum == entryCounts) {
                System.out.println("\nData yang ingin anda update adalah: ");
                System.out.println("---------------------------------------");
                System.out.println("Referensi      : " + st.nextToken());
                System.out.println("Tahun          : " + st.nextToken());
                System.out.println("Penulis        : " + st.nextToken());
                System.out.println("Penerbit       : " + st.nextToken());
                System.out.println("Judul          : " + st.nextToken());

                // update data

                // mengambil input dari user

                String[] fieldData = {"tahun","penulis","penerbit","judul"};
                String[] tempData = new String[4];

                // refresh token
                st = new StringTokenizer(data,",");
                String originalData = st.nextToken();

                for (int i=0; i < fieldData.length; i++) {
                    boolean isUpdate = Utility.getYesorNo("apakah anda ingin merubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate) {
                        // user input
                        if (fieldData[i].equalsIgnoreCase("tahun")) {
                            System.out.print("masukan tahun terbit, format=(YYYY): ");
                            tempData[i] = Utility.ambilTahun();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                            tempData[i] = terminalInput.nextLine();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                }

                // tampilkan data yang ingin diupdate
                st = new StringTokenizer(data,",");
                st.nextToken();
                System.out.println("\nData yang diupdate adalah: ");
                System.out.println("-----------------------------");
                System.out.println("Tahun          : " + st.nextToken() + " --> " + tempData[0] );
                System.out.println("Penulis        : " + st.nextToken() + " --> " + tempData[1] );
                System.out.println("Penerbit       : " + st.nextToken() + " --> " + tempData[2] );
                System.out.println("Judul          : " + st.nextToken() + " --> " + tempData[3] );

                boolean isUpdate = Utility.getYesorNo("apakah anda akan mengupdate data tersebut?");

                if (isUpdate) {

                    // cek data baru di database
                    boolean isExist = Utility.cekBukuDiDatabase(tempData, false);

                    if (isExist) {
                        System.err.println("data buku sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        bufferOutput.write(data);
                    } else {

                        // format data baru ke database
                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

                        // kita bikin primary key
                        long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;

                        String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
                        String primaryKey = penulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;

                        // tulis data ke database
                        bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                    }
                } else {
                    bufferOutput.write(data);
                }
            } else {
                // copy data
                bufferOutput.write(data);
            }

            bufferOutput.newLine();

            data = bufferInput.readLine();
        }

        // menulis data ke file
        bufferOutput.flush();

        // tutup file
        bufferOutput.close();
        fileOutput.close();
        bufferInput.close();
        fileInput.close();

        System.gc();

        // delete original file
        database.delete();
        // rename tempDB menjadi database
        tempDB.renameTo(database);

    }

    public static void deleteData() throws IOException {
        // kita ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilkanData();

        // kita ambil user input untuk mendelete data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan nomor buku yang akan dihapus: ");
        int deleteNum = terminalInput.nextInt();

        // looping untuk membaca tiap data baris dan skip data yang akan didelete

        int entryCounts = 0;
        boolean isFound = false;

        String data = bufferedInput.readLine();

        while (data != null) {
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer st = new StringTokenizer(data,",");

            // tampilkan data yang ingin di hapus
            if (deleteNum == entryCounts) {
                System.out.println("\nData yang ingin anda hapus adalah :");
                System.out.println("------------------------------------");
                System.out.println("Referensi    : " + st.nextToken());
                System.out.println("Tahun        : " + st.nextToken());
                System.out.println("Penulis      : " + st.nextToken());
                System.out.println("Penerbit     : " + st.nextToken());
                System.out.println("Judul        : " + st.nextToken());

                isDelete = Utility.getYesorNo("Apakah anda yakin ingin menghapus?");
                isFound = true;
            }

            if(isDelete) {
                // skip pindahan data dari original ke sementara
                System.out.println("Data berhasil dihapus");
            } else {
                //  kita pindahkan data dari original ke sementara
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        if (!isFound) {
            System.err.println("Buku tidak ditemukan");
        }

        // menulis data ke file
        bufferedOutput.flush();

        // tutup file
        bufferedOutput.close();
        fileOutput.close();
        bufferedInput.close();
        fileInput.close();

        System.gc();

        // delete data original
        database.delete();
        // rename file sementara ke database
        tempDB.renameTo(database);

    }

    public static void tampilkanData() throws IOException {

        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e) {
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("| No |\tTahun |\tPenulis\t\t\t\t  | Penerbit\t\t\t  |\tJudul Buku");
        System.out.println("-------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int nomorData = 0;

        while(data != null) {
            nomorData++;
            StringTokenizer stringToken = new StringTokenizer(data,",");

            stringToken.nextToken();
            System.out.printf("| %2d ", nomorData);
            System.out.printf("|\t%4s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s  ", stringToken.nextToken());
            System.out.printf("|\t%2s  ", stringToken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }

        System.out.println("-------------------------------------------------------------------------------");


    }

    public static void cariData() throws IOException {

        // membaca database ada atau tidak

        try {
            File file = new File("database.txt");
        } catch (Exception e) {
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }

        // kita ambil keyword dari user

        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukan kata kunci untuk mencari buku: ");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        // kita cek keyword dari database

        Utility.cekBukuDiDatabase(keywords,true);

    }

    public static void tambahData() throws IOException {

        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner terminalInput = new Scanner(System.in);
        String penulis, judul, penerbit, tahun;

        // mengambil input dari user
        System.out.print("masukan nama penulis: ");
        penulis = terminalInput.nextLine();
        System.out.print("masukan judul buku: ");
        judul = terminalInput.nextLine();
        System.out.print("masukan nama penerbit: ");
        penerbit = terminalInput.nextLine();
        System.out.print("masukan tahun terbit, format=(YYYY): ");
        tahun = Utility.ambilTahun();

        // cek buku di database

        String[] keywords = {tahun+","+penulis+","+penerbit+","+judul};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = Utility.cekBukuDiDatabase(keywords,false);

        // menulis buku di database
        if (!isExist) {
//            fiersabesari_2012_1,2012,fiersa besari,media kita,jejak langkah
            System.out.println(Utility.ambilEntryPerTahun(penulis, tahun));
            long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;

            String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
            String primaryKey = penulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;
            System.out.println("\nData yang akan anda masukan adalah");
            System.out.println("-------------------------------------");
            System.out.println("primary key   : " + primaryKey);
            System.out.println("tahun terbit  : " + tahun);
            System.out.println("penulis       : " + penulis);
            System.out.println("judul         : " + judul);
            System.out.println("penerbit      : " + penerbit);

            boolean isTambah = Utility.getYesorNo("Apakah anda akan menambah data tersebut?");

            if (isTambah) {
                bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("\nbuku yang akan anda masukan sudah tersedia di database dengan data berikut :");
            Utility.cekBukuDiDatabase(keywords,true);
        }

        bufferOutput.close();

    }

}
