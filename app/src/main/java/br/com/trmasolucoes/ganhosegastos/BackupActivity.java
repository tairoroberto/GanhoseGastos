package br.com.trmasolucoes.ganhosegastos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;


public class BackupActivity extends ActionBarActivity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    private ListView listViewBackup;
    private static final int FILE_SELECT_EMAIL_CODE = 1;
    private static final int BACKUP_RECOVER_CODE = 2;

    /** Variavel para verificar a permisão*/
    private static final int REQUEST_WRITE_CONTACTS_STORAGE_CALENDAR = 112;
    private boolean exportar = false;
    private String pathImportacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        /******************************************************************************************/
        /*               Inicio dos método de açao do drawer layout                              */
        /****************************************************************************************/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        /******************************************************************************************/
        /*               Fim dos método de açao do drawer layout                                 */
        /****************************************************************************************/

        ListView listaOpcoes = (ListView) findViewById(R.id.drawer_content);
        listaOpcoes.setAdapter(new ListaOpcoesAdapter(BackupActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(BackupActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                    Intent intent = new Intent(BackupActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 2){
                    Intent intent = new Intent(BackupActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    Intent intent = new Intent(BackupActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(BackupActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    //Intent intent = new Intent(BackupActivity.this,BackupActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);
                }
            }
        });

        /*******************************************************************************************/
        /*                            Inicio da lista de backup                                   */
        /*****************************************************************************************/
        listViewBackup = (ListView) findViewById(R.id.listViewBackup);
        String[] lista = resources.getStringArray(R.array.array_backup);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);

        listViewBackup.setAdapter(adapter);


        listViewBackup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){

                    exportar = true;

                    AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
                    builder.setTitle("Ganhos & Gastos Backup");

                    builder.setMessage("Deseja enviar o backup por e-mail")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    /** Verifica as permissões */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (ContextCompat.checkSelfPermission(BackupActivity.this,
                                                Manifest.permission.READ_CONTACTS)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(BackupActivity.this,
                                                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR},
                                                    REQUEST_WRITE_CONTACTS_STORAGE_CALENDAR);
                                        }else {
                                            exportDB();
                                        }
                                    }else {
                                        exportDB();
                                    }
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();

                }else{
                    showFileChooser();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case FILE_SELECT_EMAIL_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("Script", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("Script", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload

                    sendMail(path);
                }
                break;

            case BACKUP_RECOVER_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("Script", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("Script", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload

                    if (path != null && path.contains(".GG")){

                        pathImportacao = path;
                        exportar = false;

                        /** Verifica as permissões */
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(BackupActivity.this,
                                    Manifest.permission.READ_CONTACTS)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(BackupActivity.this,
                                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR},
                                        REQUEST_WRITE_CONTACTS_STORAGE_CALENDAR);
                            }else {
                                importDB(path);
                            }
                        }else {
                            importDB(path);
                        }

                    }else {
                        Toast.makeText(BackupActivity.this,"Arquivo inválido", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showFileEmailChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Selecione um arquivo de backup para ser enviado"),
                    FILE_SELECT_EMAIL_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Por favor instale um Genrenciador de arquivos.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Selecione um de backup"),
                    BACKUP_RECOVER_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Por favor instale um Genrenciador de arquivos.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null){
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }

                    cursor.close();
                }

            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }




    public void sendMail(String path) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Ganhos & Gastos "+ DateUtil.getDateToStringShort(DateUtil.getDataHoje()));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Este é um email gerado automaticamente pelo App Ganhos & Gastos com o backup a ser enviado.");
        emailIntent.setType("*/*");
        Uri myUri = Uri.parse("file://" + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void exportDB(){
        try {
            // Caminho de Origem do Seu Banco de Dados
            InputStream in = new FileInputStream(
                    new File(Environment.getDataDirectory()
                            + "/data/br.com.trmasolucoes.ganhosegastos/databases/GanhoseGastos_db"));

            String path = "/GanhoseGastos/" + DateUtil.getDateToStringShort(DateUtil.getDataHoje());
            File file = new File(Environment.getExternalStorageDirectory(), path);

            if (!file.mkdirs()) {
                Log.e("Script", "Diretorio não criado!");
            }

            // Caminho de Destino do Backup do Seu Banco de Dados
            OutputStream out = new FileOutputStream(new File(
                    Environment.getExternalStorageDirectory() + path + "/GanhoseGastos.GG"));

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Toast.makeText(BackupActivity.this,"Backup realizado com sucesso!", Toast.LENGTH_SHORT).show();
            showFileEmailChooser();
        }  catch (Exception e) {
            Toast.makeText(BackupActivity.this, "Backup falhou: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Script","Falha no Backup: " + e.getMessage());
        }
    }

    public boolean importDB(String path){

        File dbFile = new File(path);
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(dbFile);
            String outFileName = Environment.getDataDirectory()+"/data/br.com.trmasolucoes.ganhosegastos/databases/GanhoseGastos_db";

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer))>0){
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            Toast.makeText(BackupActivity.this,"Backup restaurado com sucesso!", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


     @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_CONTACTS_STORAGE_CALENDAR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted  start reading

                /** Verifica se é ŕa exportar ou importar*/
                if (exportar){
                    exportDB();
                }else {
                    importDB(pathImportacao);
                }

            } else {
                Toast.makeText(BackupActivity.this, "SEM PERMISSÃO DE ESCRITA NO SDCARD", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
