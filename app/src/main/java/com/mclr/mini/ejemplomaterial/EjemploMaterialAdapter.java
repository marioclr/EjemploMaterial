package com.mclr.mini.ejemplomaterial;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class EjemploMaterialAdapter extends RecyclerView.Adapter<EjemploMaterialAdapter.ViewHolder> {
    private static final String DEBUG_TAG = "EjemploMaterialAdapter";

    public Context context;
    public ArrayList<Recordatorio> mListaRecordatorios;

    public EjemploMaterialAdapter(Context context, ArrayList<Recordatorio> cardsList) {
        this.context = context;
        this.mListaRecordatorios = cardsList;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String name = mListaRecordatorios.get(position).getNombre();
        int importantCheck = mListaRecordatorios.get(position).getImportante();
        int color = mListaRecordatorios.get(position).getColorResource();
        TextView initial = viewHolder.initial;
        TextView nameTextView = viewHolder.name;
        Switch important = viewHolder.important;
        nameTextView.setText(name);
        important.setChecked(importantCheck>0);
        initial.setBackgroundColor(color);
        initial.setText(Character.toString(name.charAt(0)));
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        animateCircularReveal(viewHolder.itemView);
    }

    public void animateCircularReveal(View view) {
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.start();
    }

    public void animateCircularDelete(final View view, final int list_position) {
        int centerX = view.getWidth();
        int centerY = view.getHeight();
        int startRadius = view.getWidth();
        int endRadius = 0;
        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Log.d(DEBUG_TAG, "EjemploMaterialAdapter onAnimationEnd for Edit adapter position " + list_position);
                Log.d(DEBUG_TAG, "EjemploMaterialAdapter onAnimationEnd for Edit cardId " + getItemId(list_position));

                view.setVisibility(View.INVISIBLE);
                mListaRecordatorios.remove(list_position);
                notifyItemRemoved(list_position);
            }
        });
        animation.start();
    }

    public void addCard(String name, int important, int color) {
        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setNombre(name);
        recordatorio.setImportante(important);
        recordatorio.setColorResource(color);
        recordatorio.setId(getItemCount());
        mListaRecordatorios.add(recordatorio);
        ((EjemploMaterialActivity) context).doSmoothScroll(getItemCount());
        notifyItemInserted(getItemCount());
    }

    public void updateCard(String name, int important, int list_position) {
        mListaRecordatorios.get(list_position).setNombre(name);
        mListaRecordatorios.get(list_position).setImportante(important);
        Log.d(DEBUG_TAG, "list_position is " + list_position);
        notifyItemChanged(list_position);
    }

    public void deleteCard(View view, int list_position) {
        animateCircularDelete(view, list_position);
    }

    @Override
    public int getItemCount() {
        if (mListaRecordatorios.isEmpty()) {
            return 0;
        } else {
            return mListaRecordatorios.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return mListaRecordatorios.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.recordatorio_view_holder, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView initial;
        private TextView name;
        private Switch important;
        private Button deleteButton;

        public ViewHolder(View v) {
            super(v);
            initial = (TextView) v.findViewById(R.id.initial);
            name = (TextView) v.findViewById(R.id.name);
            important = (Switch) v.findViewById(R.id.importante);
            deleteButton = (Button) v.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateCircularDelete(itemView, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> p1 = Pair.create((View) initial, EjemploMaterialActivity.TRANSICION_INICIAL);
                    Pair<View, String> p2 = Pair.create((View) name, EjemploMaterialActivity.TRANSICION_NOMBRE);
                    Pair<View, String> p3 = Pair.create((View) important, EjemploMaterialActivity.TRANSITION_IMPORTANTE);
                    Pair<View, String> p4 = Pair.create((View) deleteButton, EjemploMaterialActivity.TRANSITION_BOTON_BORRAR);

                    ActivityOptionsCompat options;
                    Activity act = (AppCompatActivity) context;
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, p1, p2, p3, p4);

                    int requestCode = getAdapterPosition();
                    String name = mListaRecordatorios.get(requestCode).getNombre();
                    int important = mListaRecordatorios.get(requestCode).getImportante();
                    int color = mListaRecordatorios.get(requestCode).getColorResource();

                    Log.d(DEBUG_TAG, "EjemploMaterialAdapter itemView listener for Edit adapter position " + requestCode);

                    Intent transitionIntent = new Intent(context, TransicionEditarActivity.class);
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_NOMBRE, name);
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_IMPORTANTE, important);
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_INICIAL, Character.toString(name.charAt(0)));
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_COLOR, color);
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_ACTUALIZAR, false);
                    transitionIntent.putExtra(EjemploMaterialActivity.EXTRA_BORRAR, false);
                    ((AppCompatActivity) context).startActivityForResult(transitionIntent, requestCode, options.toBundle());
                }
            });
        }
    }
}
