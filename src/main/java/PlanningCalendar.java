import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlanningCalendar {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String CLIENT_ID = "666695915564-h4spcnmqmbljnaiv4i7mc15img41kkdi.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR);
    private static final String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";

    private static final String TOKENS_DIRECTORY_PATH = "./credentials";

    private static GoogleAuthorizationCodeFlow flow;
    static {
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int DAY_IN_MILLIS = 86400000;

    // TODO: Probably should find a way to create and use a different calendar on user's Google account
    private static final String CALENDER_ID = "primary";



    private static Scanner sc = new Scanner(System.in);

    // TODO: Main is temporary, should move Google auth stuff to User
    public static void main(String[] args) throws IOException {
        System.out.println(flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URL));
        System.out.println("Please click link to login with Google and enter token:");
        String authCode = sc.next();
        GoogleTokenResponse token = flow.newTokenRequest(authCode).setRedirectUri(REDIRECT_URL).execute();
        flow.createAndStoreCredential(token, "user");

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, flow.loadCredential("user")).setApplicationName("Planning Calendar").build();

        // Scuffed unit testing
        Goal goal = new Goal("goal 1", "a goal", 100, new Date());
        addGoal(goal, service);

        getGoals(service);
    }

    public static void addGoal(Goal goal, Calendar service) throws IOException {
        Event event = new Event();
        event.setSummary(goal.getSummary());
        event.setDescription(goal.getDescription());
        Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
        Map<String, String> properties = new HashMap<>();
        properties.put("isPlanningCalendarEvent", "true");
        properties.put("timeRequired", String.valueOf(goal.time_required));
        extendedProperties.setPrivate(properties);
        event.setExtendedProperties(extendedProperties);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTime startDateTime = new DateTime(dateFormat.format(goal.date));
        DateTime endDateTime = new DateTime(dateFormat.format(goal.date.getTime() + DAY_IN_MILLIS));

        event.setStart(new EventDateTime().setDate(startDateTime));
        event.setEnd(new EventDateTime().setDate(endDateTime));

        event = service.events().insert(CALENDER_ID, event).execute();

        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    // Shamelessly stolen from https://developers.google.com/calendar/api/v3/reference/events/list
    public static void getGoals(Calendar service) throws IOException {
        String pageToken = null;
        do {
            Events events = service.events().list("primary").setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println(event.getSummary() + event.getExtendedProperties());
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
    }



}
