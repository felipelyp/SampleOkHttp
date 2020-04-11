package com.zzflow.statesview;

import android.app.Activity;
import android.os.StrictMode;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ServiceApi implements Callback {

    private Activity activity;

    public ServiceApi(Activity activity) {
        this.activity = activity;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();

        StrictMode.setThreadPolicy(policy);
    }

    /**
     * OkHttpCliente usado para as requisição
     */
    private OkHttpClient client = new OkHttpClient();

    /**
     * Quando objeto {@link Resultado}
     * for criado em {@link ServiceApi#onResultado}
     * e usado pelo onResulta o metodo
     * {@link ServiceApi#request(Request)} vai usa esse objeto
     * para passa os dados da requisição
     */
    private Resultado resultado;

    /**
     * Muito importante chama esse metodo
     * antes de fazer as requisição
     * para você obter os resultados
     *
     * @param resultado crie um new Resultado() { ... }
     */
    public void onResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    /**
     * Fazer um POST
     *
     * @param url  URL do POST
     * @param body Dados para o POST
     */
    public void post(String url, String body) {
        //Obter tipo de formulario
        MediaType mediaType = checkMediaType(body);

        RequestBody requestBody = RequestBody.create(body, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        request(request);
    }

    /**
     * Fazer um GE
     *
     * @param url URL para solicitar
     */
    public void get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        request(request);
    }

    /**
     * Fazer requisição
     *
     * @param request dados para requisição
     */
    private void request(Request request) {
        client.newCall(request).enqueue(this);
    }

    /**
     * Verificar se é JSON
     *
     * @return o tipo de formulario
     */
    private MediaType checkMediaType(String body) {
        try {
            new JSONObject(body);
            // { "status": 1 }
            return MediaType.get("application/json");
        } catch (JSONException e) {
            // nome=ZzFlow&senha=1234
            return MediaType.get("application/x-www-form-urlencoded");
        }
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        prepareResponse(null);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull final Response response) {
        prepareResponse(response);
    }

    private void prepareResponse(final Response response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Boolean success = response.isSuccessful();
                    String result = response.body().string();

                    resultado.onResponse(result, success);
                } catch (Exception error) {
                    resultado.onResponse(null, false);
                }
            }
        });
    }

    public interface Resultado {

        void onResponse(@Nullable String response, Boolean sucesso);
    }
}
