package com.example.proyecto_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BaseDatos extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;//declaración variable almacena la versión BB.DD.
    public static final String DATABASE_NAME = "PalabrasOposicion.db";//Declaración de la variable que almacena el nombre de la BB.DD.

    public static final String TABLE_NAME = "palabras";//Declaración de la variable que almacena el nombre de la tabla
    public static final String COLUMN_NAME_ID = "id";//Declaración de la variable que almacena el nombre de la columna "ID"
    public static final String COLUMN_NAME_PALABRA = "palabra";//Declaración de la variable que almacena el nombre de la columna "palabra"
    public static final String COLUMN_NAME_VALOR = "valor";//Declaración de la variable que almacena el nombre de la columna "valor"

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    //Declaración constante que almacena la sentencia para crear la tabla de la BB.DD.
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_PALABRA + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_VALOR + TEXT_TYPE + " )";
    //Declaración constante que almacena la sentencia para eliminar la tabla de la BB.DD.
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public BaseDatos(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }//final constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);//ejecución sentencia de creación de la tabla de la BB.DD.
    }//final onCreate

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Método que actualiza la BB.DD. en caso de cambio de versión.
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }//final onUpgrade

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }//final onDowngrade

    public String getID(String palabra){
        String id = null;//string que almacena el resultado obtenido de la búsqueda.

        SQLiteDatabase bd = this.getReadableDatabase();//apertura de la BB.DD. en modo lectura.
        String[] projection = {this.COLUMN_NAME_ID};//columna de la que se quiere obtener su valor
        String selection = this.COLUMN_NAME_PALABRA + " = ?";//criterio de búsqueda
        String[] selectionArgs = { palabra };//valor para el criterio de la búsqueda

        try {
            Cursor cursor = bd.query(       //Operación de consulta a la BB.DD.
                    BaseDatos.TABLE_NAME,   //Tabla sobre la que se realiza la búsqueda
                    projection,             //Columna de la cual se desea obtener su valor
                    selection,              //Columna por la que se realiza la búsqueda (de la cláusula where)
                    selectionArgs,          //Valor con el que se realiza la búsqueda (de la cláusa where)
                    null,
                    null,
                    null
            );//final de consulta
            cursor.moveToFirst();//posicionamiento del cursor en el primer registro del resultSet obtenido.
            id = cursor.getString(0);
        } catch (Exception e){}//final try catch.

        bd.close();//cierre de la conexión con la BB.DD.

        return id;
    }//final getID

    public String getPalabra(String id){
        String palabra = "false";//string que almacena el resultado obtenido de la búsqueda.

        SQLiteDatabase bd = this.getReadableDatabase();//apertura de la BB.DD. en modo lectura.
        String[] projection = {this.COLUMN_NAME_PALABRA};//columna de la que se quiere obtener su valor
        String selection = this.COLUMN_NAME_ID + " = ?";//criterio de búsqueda
        String[] selectionArgs = { id };//valor para el criterio de la búsqueda

        try {
            Cursor cursor = bd.query(       //Operación de consulta a la BB.DD.
                    BaseDatos.TABLE_NAME,   //Tabla sobre la que se realiza la búsqueda
                    projection,             //Columna de la cual se desea obtener su valor
                    selection,              //Columna por la que se realiza la búsqueda (de la cláusula where)
                    selectionArgs,          //Valor con el que se realiza la búsqueda (de la cláusa where)
                    null,
                    null,
                    null
            );//final de consulta
            cursor.moveToFirst();//posicionamiento del cursor en el primer registro del resultSet obtenido.
            palabra = cursor.getString(0);
        } catch (Exception e){}//final try catch.

        bd.close();//cierre de la conexión con la BB.DD.

        return palabra;
    }//final getPalabra

    public void initTabla(Context contexto){
        SQLiteDatabase bd = this.getWritableDatabase();//se abre la BB.DD. en modo escritura.
        ContentValues valuesCorrect = new ContentValues();
        ContentValues valuesIncorrect = new ContentValues();

        String lineaFiCorrect;//declaración varible que almacena cada palabra del fichero txt leído.
        String lineaFiIncorrect;//declaración varible que almacena cada palabra del fichero txt leído.
        int id = 0;//declaración variable que se utiliza para pasar el valor id a los registros que se creen en la tabla de la BB.DD.
        int i;//declaración variable que se  utiliza como contador del bucle for.

        InputStream correctasFic = contexto.getResources().openRawResource(R.raw.correctas);//flujo de lectura del fichero txt con las palabras correctas.
        InputStream inCorrectasFic = contexto.getResources().openRawResource(R.raw.incorrectas);//flujo de lectura del fichero txt con las palabras incorrectas.
        BufferedReader bufferCorrectas = new BufferedReader(new InputStreamReader(correctasFic));
        BufferedReader bufferIncorrectas = new BufferedReader(new InputStreamReader(inCorrectasFic));

        if(bd != null){
            try{
                for(i = 0; i < 100; i++){
                    lineaFiCorrect = bufferCorrectas.readLine();//lectura del fichero txt con las palabras correctas.
                    valuesCorrect.put(BaseDatos.COLUMN_NAME_ID, String.valueOf(id));
                    valuesCorrect.put(BaseDatos.COLUMN_NAME_PALABRA, lineaFiCorrect);
                    valuesCorrect.put(BaseDatos.COLUMN_NAME_VALOR, "0");
                    long newRowIdCorrect = bd.insert(BaseDatos.TABLE_NAME, null, valuesCorrect);

                    id++;//aumento de la variable que almacena los ID de cada registro de la tabla de la BB.DD.

                    lineaFiIncorrect = bufferIncorrectas.readLine();//lectura del fichero txt con las palabras incorrectas.
                    valuesIncorrect.put(BaseDatos.COLUMN_NAME_ID, String.valueOf(id));
                    valuesIncorrect.put(BaseDatos.COLUMN_NAME_PALABRA, lineaFiIncorrect);
                    valuesIncorrect.put(BaseDatos.COLUMN_NAME_VALOR, "1");
                    long newRowIdIncorrect = bd.insert(BaseDatos.TABLE_NAME, null, valuesIncorrect);

                    id++;//aumento de la variable que almacena los ID de cada registro de la tabla de la BB.DD.
                }//final del for que recorre las 100 líneas de cada fichero de palabras y rellena la tabla de la BB.DD.
            }catch(IOException e){
                i = 121;//Instrucción para salir del bucle en caso de producirse una excepción.
            }//final try catch.
            bd.close();//se cierra la conexión con la BB.DD.
        }//final if que controla que se haya abierto la conexión con la BB.DD. correctamente.

        if(bufferCorrectas != null){
            try{
                bufferCorrectas.close();
            }catch(IOException ex){ }
        }//final if cierra buffer

        if(correctasFic != null){
            try{
                correctasFic.close();
            }catch(IOException ex){ }
        }//final if cierra stream

        if(bufferIncorrectas != null){
            try{
                bufferIncorrectas.close();
            }catch(IOException ex){ }
        }//final if cierra buffer

        if(inCorrectasFic != null){
            try{
                inCorrectasFic.close();
            }catch(IOException ex){ }
        }//final if cierra stream

    }//final initTabla

}//final class BaseDatos.
