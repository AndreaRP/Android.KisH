package maps.rubio.andrea.kantoishere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

/**
 * Clase customizada de WebViewClient que permite gestionar el envio de emails.
 * @author Andrea
 */
public class MiWebView extends WebViewClient {
    private final WeakReference<Activity> mActivityRef;

    public MiWebView(Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
    }

    /**
     * Si se trata de un email, crea un intent especifico con el remitente que aparezca en la url.
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("mailto:")) {
            final Activity activity = mActivityRef.get();
            if (activity != null) {
                MailTo mt = MailTo.parse(url);
                Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                activity.startActivity(i);
                view.reload();
                return true;
            }
        } else {
            view.loadUrl(url);
        }
        return true;
    }

    private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }
}
