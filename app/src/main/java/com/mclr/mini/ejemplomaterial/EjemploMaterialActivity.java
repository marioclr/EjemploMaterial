package com.mclr.mini.ejemplomaterial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class EjemploMaterialActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AppCompatActivity";

    public static final String EXTRA_ACTUALIZAR = "actualizar";
    public static final String EXTRA_BORRAR = "borrar";
    public static final String EXTRA_NOMBRE = "nombre";
    public static final String EXTRA_IMPORTANTE = "importante";
    public static final String EXTRA_COLOR = "color";
    public static final String EXTRA_INICIAL = "inicial";

    public static final String TRANSITION_FAB = "fab_transicion";
    public static final String TRANSICION_INICIAL = "inicial_transicion";
    public static final String TRANSICION_NOMBRE = "nombre_transicion";
    public static final String TRANSITION_IMPORTANTE = "importante_transicion";
    public static final String TRANSITION_BOTON_BORRAR = "boton_borrar_transicion";

    private RecyclerView recyclerView;
    private EjemploMaterialAdapter adapter;
    private ArrayList<Recordatorio> mListaRecordatorios = new ArrayList<>();
    private int[] colores;
    private String[] nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        nombres = getResources().getStringArray(R.array.names_array);
        colores = getResources().getIntArray(R.array.initial_colors);

        inicializaEventos();

        if (adapter == null) {
            adapter = new EjemploMaterialAdapter(this, mListaRecordatorios);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<View, String> pair = Pair.create(v.findViewById(R.id.fab), TRANSITION_FAB);

                ActivityOptionsCompat options;
                Activity act = EjemploMaterialActivity.this;
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);

                Intent transitionIntent = new Intent(act, TransicionAgregarActivity.class);
                act.startActivityForResult(transitionIntent, adapter.getItemCount(), options.toBundle());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(DEBUG_TAG, "requestCode is " + requestCode);
        // if adapter.getItemCount() is request code, that means we are adding a new position
        // anything less than adapter.getItemCount() means we are editing a particular position
        if (requestCode == adapter.getItemCount()) {
            if (resultCode == RESULT_OK) {
                // Make sure the Add request was successful
                // if add name, insert name in list
                String name = data.getStringExtra(EXTRA_NOMBRE);
                int important = data.getIntExtra(EXTRA_IMPORTANTE, 0);
                int color = data.getIntExtra(EXTRA_COLOR, 0);
                adapter.addCard(name, important, color);
            }
        } else {
            // Anything other than adapter.getItemCount() means editing a particular list item
            // the requestCode is the list item position
            if (resultCode == RESULT_OK) {
                // Make sure the Update request was successful
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(requestCode);
                if (data.getExtras().getBoolean(EXTRA_BORRAR, false)) {
                    // if delete user delete
                    // The user deleted a contact
                    adapter.deleteCard(viewHolder.itemView, requestCode);
                } else if (data.getExtras().getBoolean(EXTRA_ACTUALIZAR)) {
                    // if name changed, update user
                    String name = data.getStringExtra(EXTRA_NOMBRE);
                    int important = data.getIntExtra(EXTRA_IMPORTANTE, 0);
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    adapter.updateCard(name, important, requestCode);
                }
            }
        }
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    private void inicializaEventos() {
        for (int i = 0; i < 50; i++) {
            Recordatorio recordatorio = new Recordatorio();
            recordatorio.setId((long) i);
            recordatorio.setNombre(nombres[i]);
            if (i%2 == 0)
                recordatorio.setImportante(1);
            else
                recordatorio.setImportante(0);
            recordatorio.setColorResource(colores[i]);
            Log.d(DEBUG_TAG, "Recordatorio creado con id " + recordatorio.getId() + ", nombre " + recordatorio.getNombre() + ", color " + recordatorio.getColorResource());
            mListaRecordatorios.add(recordatorio);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
