package com.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GmailApp {
    private static final String APPLICATION_NAME = "Gmail API Example";     //nome
    private static final JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();    //specifica la library JSON

    public static Gmail getService() throws GeneralSecurityException, IOException {
        //carica le credenziali
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/progproj-api-425e36aca9a0.json"))
                .createScoped(Collections.singleton(GmailScopes.GMAIL_READONLY));       //Dà autorizzazione per LEGGERE i messaggi gmail

        //costruisce il servizio Gmail e ritorna un oggetto del servizio Gmail che verrà utilizzato per effettuare chiamate alle API
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),       //serve per una comunicazione sicura coi server Google
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)             //adatta le credenziali da utilizzare nelle richieste Http
        ).setApplicationName(APPLICATION_NAME).build();             //.build() crea l'oggetto effettivo del servizio gmail
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Gmail service = getService();       //Ottieni il servizio gmail (crea un object che rappresenta il servizio Gmail, usato per interagire con le API di Google; in questo caso leggere ed eliminare messaggi)

        //elenco dei messaggi nella casella di posta
        ListMessagesResponse messagesResponse = service.users().messages().list("me").execute();  //'me' si riferisce all'owner delle credenziali

        //stampa tutti i messaggi
        if (messagesResponse.getMessages() != null) {
            for (Message message : messagesResponse.getMessages()) {
                System.out.println("Messaggio ID: " + message.getId());

                        /*  
                        --- Si potrebbe creare un metodo che ottiene il contenuto della mail, per poi chiamarlo dentro questo ciclo for:  
                        
                                getEmailContent(service, messageId);

                        --- Poi controllare se è spam:

                                if (isSpam(message)) {
                                    System.out.println("Messaggio contrassegnato come spam.");
                            
                        ---  Ed infine eliminarlo qua:
                
                                    deleteEmail(service, messageId);
                        */
            }
        } else {
            System.out.println("Nessun messaggio trovato.");
        }
    }
}

/*
- Recuperare contenuti delle email: Usa service.users().messages().get("me", messageId).execute() per ottenere il contenuto di un'email.
- Classificare email come spam: Integra un algoritmo di machine learning o una libreria per analizzare il contenuto dei messaggi.
- Eliminare email: Usa service.users().messages().delete("me", messageId).execute() per eliminare i messaggi.
 */