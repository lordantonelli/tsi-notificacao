package com.example.tsi.notificacao;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import java.util.List;

public class NotificationUtil {

    public static final String ACTION_VISUALIZAR = "com.example.tsi.notificacao.ACTION_VISUALIZAR";

    static final String CHANNEL_ID = "1";

    // Cria a PendingIntent para abrir a activity da intent
    private static PendingIntent getPendingIntent(Context context, Intent intent, int id) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context );
        // Esta linha mantém a activity pai na pilha de activities
        stackBuilder.addParentStack(intent.getComponent());
        // Configura a intent que vai abrir ao clicar na notificação
        stackBuilder.addNextIntent(intent);
        // Cria a PendingIntent e atualiza caso exista uma com o mesmo ID
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    // Cria uma modelo de notificação a ser enviada
    private static NotificationCompat.Builder createNotification(Context context, Intent intent,
                                                                 String contentTitle,
                                                                 String contentText, int id) {
        // Cria a PendingIntent (contém a intent original)
        PendingIntent p = getPendingIntent(context, intent, id);
        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context,CHANNEL_ID);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configurações padrão
        b.setSmallIcon(R.drawable.ic_notification_icon); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela
        return b;
    }

    // Dispara a notificação para o sistema
    private static void notify(Context context, NotificationCompat.Builder b, int id) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        // O ID serve para cancelar manualmente a notificação se necessário
        nm.notify(id, b.build());
    }

    // Criar canal de comunicação para Android O ou superior
    public static void createChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel c = new NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            c.setLightColor(Color.BLUE);
            c.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            // c.enableVibration(false);
            // c.enableLights(false);
            // c.setSound();
            manager.createNotificationChannel(c);
        }
    }

    // Cria uma notificação simples
    public static void create(Context context, Intent intent,
                              String contentTitle, String contentText, int id) {
        // Cria a notificação
        NotificationCompat.Builder b = createNotification(context,
                intent, contentTitle, contentText, id);
        b.setColor(Color.GREEN);
        // Dispara a notificação para o sistema
        notify(context, b, id);
    }

    // Notificação heads-up
    public static void createHeadsUpNotification(Context context, Intent intent,
                                                 String contentTitle, String contentText, int id) {
        // Cria a notificação
        NotificationCompat.Builder b = createNotification(context, intent,
                contentTitle, contentText, id);
        b.setColor(Color.RED);
        // Heads-up notification
        b.setFullScreenIntent(getPendingIntent(context, intent, id), false);
        b.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        // Dispara a notificação para o sistema
        notify(context, b, id);
    }

    // Notificação com ação
    public static void createWithAction(Context context, Intent intent,
                                        String contentTitle, String contentText, int id) {
        // Cria a notificação
        NotificationCompat.Builder b = createNotification(context, intent,
                contentTitle, contentText, id);
        // Ação customizada (deixei a mesma intent para os dois)
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_VISUALIZAR), 0);
        b.addAction(R.drawable.ic_acao_pause, "Pause", actionIntent);
        b.addAction(R.drawable.ic_acao_play, "Play", actionIntent);
        // Dispara a notificação para o sistema
        notify(context, b, id);
    }

    // Notificação grande
    public static void createBig(Context context, Intent intent, String contentTitle,
                                 String contentText,List<String> lines, int id) {
        // Configura o estilo Inbox
        int size = lines.size();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(contentTitle);
        for (String s: lines) { inboxStyle.addLine(s); }
        inboxStyle.setSummaryText(contentText);
        // Cria a notificação
        NotificationCompat.Builder b = createNotification(context, intent, contentTitle, contentText, id);
        b.setNumber(size); // Número para aparecer na notificação
        b.setStyle(inboxStyle); // Estilo customizado
        // Dispara a notificação para o sistema
        notify(context, b, id);
    }

    // Notificação com tela de bloqueio
    public static void createPrivateNotification(Context context, Intent intent,
                                                 String contentTitle, String contentText, int id) {
        // Cria a notificação
        NotificationCompat.Builder b = createNotification(context, intent,
                contentTitle, contentText, id);
        b.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        // Dispara a notificação para o sistema
        notify(context, b, id);
    }

    public static void cancell(Context context, int id) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }

    public static void cancellAll(Context context) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancelAll();
    }

}
