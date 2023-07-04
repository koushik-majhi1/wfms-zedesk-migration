

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okhttp3.internal.http2.StreamResetException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.server.reactive.SslInfo;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Map.entry;

public class Main {

    enum Endpoint {
        URL,
        API
    }

    static String env = "stage";
    static String stageEndpoint = "https://h-dev-apigateway.byjus.onl/";
    static String prodEndpoint = "https://api.byjus.com/";
    static String Endpoint  = env=="prod"?prodEndpoint:stageEndpoint;
    private static String stageSessionToken="c5bc045b73a4e46929b925a11bb164faf0b24143581c0facb55344109aed9da3";
    private static String prodSessionToken="85965db0cc274978ec01f39f966213bcee719a46d01d0d5f26e76259270efdbc";
    static String SessionToken = env == "prod" ? prodSessionToken : stageSessionToken;
    static String ProjectID  = env=="prod"?"16094":"999993";
    static String ProjectPrefix  = env=="prod"?"":"";

    private static final Object lock = new Object();
    static String DownloadedDataPath = "/Users/tnluser/Desktop/code/random/MigrationScripts/ZendeskMigration/Data/Downloaded/";
    static String CompiledDataPath = "/Users/tnluser/Desktop/code/random/MigrationScripts/ZendeskMigration/Data/Compiled-" + env +"/";
    static ArrayList<String> issueType = new ArrayList<>();
    static HashMap<String, Root.User> users;
    static HashMap<String, Root.GroupMembership> group_memberships;
    static HashMap<String, Root.Group> groups;
    static HashMap<String, Root.Trigger> triggers;
    static HashMap<String, Root.Trigger> wfmsTriggers;
    static HashMap<String, WFMSObjects.Project> wfmsProjects;
    static HashMap<Integer, String> unassignedGroups;
    static HashMap<String, WFMSObjects.Group> wfmsGroups;
    static HashMap<String, Root.TicketField> fields;
    static HashMap<String, Root.TicketForm> forms;
    static HashMap<String, WFMSObjects.Form> wfmsForms;
    static ConcurrentHashMap<String, MigratedTicketInfo> migratedTicketInfo;
    static HashMap<String, Integer> mapFormsCategory;
    static Map<String, String> projectGroups;

    static ArrayList<String> issueSubType = new ArrayList<>();


    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    static Map<String, String> dictDataToDownload = Map.ofEntries(
            entry("groups", "groups")
            , entry("group_memberships", "group_memberships")
            , entry("ticket_fields", "ticket_fields")
            , entry("ticket_forms", "ticket_forms")
            , entry("triggers", "triggers")
    );

    static ExecutorService exec = Executors.newFixedThreadPool(20);

    static int i;

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .build();



    public static void main(String[] args) throws Exception {
        try {
            //AddData();
            //LoadData();
            //DownloadDate();
            //SaveWFMSModels();
            //TestTheories();
            //CreateAndASaveTickets();
            GetAllUsers();
            //TransformTriggers();
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void TransformTriggers() {

    }

    private static void GetAllUsers() throws Exception {
        LoadData();
        ConcurrentHashMap<Integer,Root.User> usersss = new ConcurrentHashMap<>();
        HashSet<String> str1  = new HashSet<>();
        HashSet<String> str2  = new HashSet<>();
        HashSet<String> str3  = new HashSet<>();
        var users = Files.readAllLines(Path.of(CompiledDataPath + "uusers22K.csv"));
        for (var user : users) {
            //if(user.active && user.suspended==false && user.email!=null&& user.email.contains("byjus.com") && user.role.contains("end-user") )
            {
                //if(user.updated_at.getYear()==123 && str.contains(user.email))
                {
                    AtomicInteger ttt = new AtomicInteger(0);
                    exec.submit(new Runnable() {
                        @Override
                        public void run() {
                            String json="";
                            WFMSObjects.Group.Agent agent = new WFMSObjects.Group.Agent();
                            //FillNames(user.name, agent);

                            try {

                                json =  CheckIfUserValid( user);
                                ArrayList a =  (new Gson()).fromJson(json, ArrayList.class);

                                if (((ArrayList) ((ArrayList) a.get(1)).get(0)).size() == 3) {
                                    str1.add(user);
                                    //str.add(agent.firstName.replace(',', ' ') + ", " + agent.lastName.replace(',', ' ') + ", " + user.email + ", " + user.updated_at);
                                    //usersss.put(usersss.size(),user);
                                    //System.out.println(str1.size());
                                }
                                else
                                {
                                    str2.add(user);
                                }
                                System.out.print(".");
                            } catch (Exception ex) {
                                System.out.print(user);
                                str3.add(user);
                                System.out.print(json);
                                System.out.print(ex.toString());
                            }
                            if(str1.size()%10==0)
                            {
                                try {
                                    System.out.println(str1.size() + "  " + str2.size()+"   " + str3.size() );
                                    Files.write(Path.of(CompiledDataPath + "uusers22K1.csv"), str1);
                                    Files.write(Path.of(CompiledDataPath + "uusers22K2.csv"), str2);
                                    Files.write(Path.of(CompiledDataPath + "uusers22K3.csv"), str3);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }


                    });

                }

            }
        }
        Files.write(Path.of(CompiledDataPath + "uusers22K1.csv"), str1);
        Files.write(Path.of(CompiledDataPath + "uusers22K2.csv"), str2);
        Files.write(Path.of(CompiledDataPath + "uusers22K3.csv"), str3);
        //usersss.add(new Root.User());
    }

    private static String CheckIfUserValid(String email) throws Exception {

        MediaType mediaType = MediaType.parse("application/json+protobuf");
        RequestBody body = RequestBody.create(mediaType, "[[3,\"1\"],[[[[1,\""+email+"\"]],[1]]]]");
        Request request = new Request.Builder()
                .url("https://peoplestack-pa.clients6.google.com/$rpc/social.people.backend.service.intelligence.proto.PeopleStackIntelligenceService/GetAssistiveFeatures")
                .method("POST", body)
                .addHeader("authority", "peoplestack-pa.clients6.google.com")
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
                .addHeader("authorization", "SAPISIDHASH 1688199410_8b32cc1ed4ccf274741a8e55e48a77ceb8ea824d")
                .addHeader("content-type", "application/json+protobuf")
                .addHeader("cookie", "SID=YAj3G4-mLjv_fFaQHJAKrXLEHbE3gdJX6ehrt9gV7wyAMB5XoMo8L4_YpbHw0Yu-3uyPgA.; __Secure-1PSID=YAj3G4-mLjv_fFaQHJAKrXLEHbE3gdJX6ehrt9gV7wyAMB5X9iATXlwineCDGUKtHDlpHA.; __Secure-3PSID=YAj3G4-mLjv_fFaQHJAKrXLEHbE3gdJX6ehrt9gV7wyAMB5X8iZEmfCneeDUhsifQP5VVA.; HSID=APqs99QqpM3duSJoi; SSID=A3zvsSKNkKkykzsAK; APISID=dcEnQ-8MAJjp68QI/AQFAVooO5Nz0KXpr5; SAPISID=wX7QzU_x-Aa4pUMN/AbkXPbM1E-SviiEwc; __Secure-1PAPISID=wX7QzU_x-Aa4pUMN/AbkXPbM1E-SviiEwc; __Secure-3PAPISID=wX7QzU_x-Aa4pUMN/AbkXPbM1E-SviiEwc; SEARCH_SAMESITE=CgQIzpgB; AEC=AUEFqZdjYF6it0GaCo0bn5iRb1y9MYjvZtR5BqKLTVpB--KgDwtcsKZaf9I; 1P_JAR=2023-06-29-09; NID=511=vdUTzOqTKHJrWg7PyO34oQ9WV2zyilO9NB6CDgvgJ4PzZR_6uHFqMAeMpEDMYtT07Kh8m_Am8U86Iy5g0g23OBn2VyTGZlDAyxONlwwnvCwwRpGTezzsG978XgtV336heWIkN4NF5yA3_--z0UXxoXDv9h32h8yGgISumCRwTPIA2Ad4dOwweg12X-xRKpvfhIa2XNyaG9nB5Rxvrynk-xGKqw4dt8HZKL48OIamhJMiWGLzy3OVwv2pznpaemyluXWFaKAujLC2JyrSgzxv8RK1M_J-XaRucpKItQMI4v81Gr3kDLY; SIDCC=AP8dLtyuYiBsMDJatbmAflUH7yj0idVGTYv59kWL02pnWpTee7t1IQsHHt85lkhcbzR5h0JzjZY; __Secure-1PSIDCC=AP8dLtz-kLXKne-WJIsbpclQBntK6hTiKkUDtWWPxsm72BrVGMdn7Aj2aIdD8s4P1QHItuJzTA5u; __Secure-3PSIDCC=AP8dLtw56QFOfPCDBuAYIalPvYr6DFi08t-kCVXpuhNu_HF0BlzZG78_FFliKAKVXmF83VOSXDNr; SIDCC=AP8dLtwXKu9Z2XHD7EHZgOAyMb5lBnYBrYrK0A-4yTru5hCxkdM38uoB4vKYu15VxeR5_IJiVpo; __Secure-1PSIDCC=AP8dLtwPbIW-lIPZp0NaDP-IhBITxToab-tdtITtqp31OLb2hyvjYx0K2U98xeUWaFvFBcMuV_No; __Secure-3PSIDCC=AP8dLtxb_yYGbEcxKmRSrrnBTvy2lmY4yOzeEgh2aE2Kg_QrFCYGZ2ViGa8tHSJUD7JntbZh46_z")
                .addHeader("origin", "https://contacts.google.com")
                .addHeader("referer", "https://contacts.google.com/")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                .addHeader("sec-ch-ua-arch", "\"arm\"")
                .addHeader("sec-ch-ua-bitness", "\"64\"")
                .addHeader("sec-ch-ua-full-version", "\"113.0.5672.126\"")
                .addHeader("sec-ch-ua-full-version-list", "\"Google Chrome\";v=\"113.0.5672.126\", \"Chromium\";v=\"113.0.5672.126\", \"Not-A.Brand\";v=\"24.0.0.0\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-model", "\"\"")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("sec-ch-ua-platform-version", "\"13.4.0\"")
                .addHeader("sec-ch-ua-wow64", "?0")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-site")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                .addHeader("x-client-data", "CKq1yQEIhbbJAQijtskBCKmdygEI6d7KAQiTocsBCPyqzAEImv7MAQi1lM0BCOuYzQEIz5rNAQihnM0BCIagzQEIyarNAQjcq80BCKCtzQEIy63NAQi/sc0BCLayzQEI6bPNAQ==")
                .addHeader("x-goog-api-key", "AIzaSyDNHBYPk694E-O5AevTntVxBmwow9xlpCM")
                .addHeader("x-user-agent", "grpc-web-javascript/0.1")
                .build();
        Response response = client.newCall(request).execute();
        return  response.body().string();
    }

    private static void TestTheories() throws Exception {
        LoadData();
        HashMap<Object, Integer> set = new HashMap<>();
        HashMap<Object, Integer> sett = new HashMap<>();
        ArrayList<Root.Trigger> inactivetriggers = new ArrayList<>();
        ArrayList<Root.Trigger> noactionstriggers = new ArrayList<>();
        ArrayList<Root.Trigger> noconditionstriggers = new ArrayList<>();

        RemoveUnsupportedTriggers(set,sett);
        int ttt=0;
        for (var trigger : triggers.values()) {
            if (trigger.active == false) {
                wfmsTriggers.remove(trigger.id);
                inactivetriggers.add(trigger);
            } else if (trigger.actions.size() == 0) {
                wfmsTriggers.remove(trigger.id);
                noactionstriggers.add(trigger);
            } else if (trigger.conditions.any.size() == 0 && trigger.conditions.all.size() == 0) {
                wfmsTriggers.remove(trigger.id);
                noconditionstriggers.add(trigger);
            }

            boolean isexist = false;
            for (var ite : trigger.conditions.all) {
                if (ite.field.contains("custom")) {
                    isexist = true;
                }
            }
            for (var ite : trigger.conditions.any) {
                if (ite.field.contains("custom")) {
                    isexist = true;
                }
            }
            if (isexist==false) {
                ttt++;
                triggersswithoutassignment.add(trigger);
            }
        }

        int b = 0, a = 0,c=0,d=0;
      for (var trigger : triggers.values()) {
          int aa=0;
          if (wfmsTriggers.get(trigger.id).conditions.all.size() != trigger.conditions.all.size() ||
                  wfmsTriggers.get(trigger.id).conditions.any.size() != trigger.conditions.any.size()) {
              a++;
              aa++;
          }
          if (wfmsTriggers.get(trigger.id).actions.size() != trigger.actions.size()) {
              b++;
              aa++;
          }
          if (wfmsTriggers.get(trigger.id).conditions.all.size() == trigger.conditions.all.size() &&
                  wfmsTriggers.get(trigger.id).conditions.any.size() == trigger.conditions.any.size() &&
                  wfmsTriggers.get(trigger.id).actions.size() == trigger.actions.size()) {
              c++;
          }
          if(aa==2)
          {
              d++;
          }
      }

        for (var trigger : triggersswithoutassignment) {
                wfmsTriggers.remove(trigger.id);
        }
        SaveFile(GetFilePath("triggers"), triggers.values());
        LoadData();
    }

    static HashSet<Root.Trigger> triggersswithoutassignment=  new HashSet<>();
    static int tt=0;
    private static void RemoveUnsupportedTriggers(HashMap<Object, Integer> integerHashMap,HashMap<Object, Integer> integerHashMap2) throws Exception {

        var wfmsFields = Files.readAllLines(Path.of(GetCompiledFilePath("Fields")));
        HashSet<String> customFields = new HashSet<>();
        for (var field : wfmsFields) {
            for (var rootfield : fields.values()) {
                if (rootfield.title.equals(field)) {
                    customFields.add("custom_fields_" + rootfield.id);
                }
            }
        }

        for (var trigger : triggers.values()) {
            ArrayList<Root.Trigger.Action> actions = new ArrayList<>();
            boolean isgroupPresent = false;
            boolean isagentPresent = false;
            for (var action : trigger.actions) {
                if (integerHashMap.containsKey(action.field)) {
                    integerHashMap.put(action.field, integerHashMap.get(action.field) + 1);
                } else {
                    integerHashMap.put(action.field, 1);
                }

                if (action.field.contains("group_id")) {
                    if (groups.containsKey(action.value) == false || wfmsGroups.containsKey(groups.get(action.value).name) == false) {
                            isgroupPresent = true;
                            triggersswithoutassignment.add(trigger);
                    }
                }
                if (action.field.contains("assignee_id")) {
                    isagentPresent = true;
                    if(users.containsKey(action.value)==false || gson.toJson(wfmsGroups).contains(users.get(action.value).email)==false) {
                        tt++;
                        triggersswithoutassignment.add(trigger);
                    }
                    else
                    {
                    }
                }

                if (action.field.contains("custom_fields") && customFields.contains(action.field)) {
                } else if (action.field.contains("group_id") ||
                        action.field.contains("custom_status_id") ||
                        action.field.contains("priority") ||
                        action.field.contains("assignee_id") ||
                        action.field.contains("status")) {
                } else if (action.field.contains("notification") ||
                        action.field.contains("current_tags") ||
                        action.field.contains("brand_id") ||
                        action.field.contains("ticket_form_id") ||
                        action.field.contains("deflection") ||
                        action.field.contains("follower") ||
                        action.field.contains("remove_tags") ||
                        action.field.contains("side_conversation")) {
                    actions.add(action);
                } else {
                    actions.add(action);
                }
            }
            if(isgroupPresent)
            {
                //tt++;
            }
            for (var action : actions) {
                trigger.actions.remove(action);
            }
            RemoveConditions(customFields,trigger, trigger.conditions.all,integerHashMap2);
            RemoveConditions(customFields,trigger, trigger.conditions.any,integerHashMap2);
        }
    }

    @NotNull
    private static void RemoveConditions(HashSet<String> customFields,
                                         Root.Trigger trigger,
                                         ArrayList<Root.Trigger.TriggerConditions.Contidion> triggerConditions,
                                         HashMap<Object, Integer> integerHashMap) {
        ArrayList<Root.Trigger.TriggerConditions.Contidion> conditions = new ArrayList<>();
        for (var action : triggerConditions) {
            if (integerHashMap.containsKey(action.field)) {
                integerHashMap.put(action.field, integerHashMap.get(action.field) + 1);
            } else {
                integerHashMap.put(action.field, 1);
            }
            if (action.field.startsWith("custom_fields") && customFields.contains(action.field)) {
            } else if (action.field.contains("group_id") ||
                    action.field.contains("custom_status_id") ||
                    action.field.contains("priority") ||
                    action.field.contains("assignee_id") ||
                    action.field.contains("status")) {
            } else if (action.field.contains("custom_fields") ||
                    action.field.equals("role") ||
                    action.field.equals("ticket_is_public") ||
                    action.field.equals("current_tags") ||
                    action.field.equals("brand_id") ||
                    action.field.equals("current_via_id") ||
                    action.field.equals("organization_id") ||
                    action.field.equals("recipient") ||
                    action.field.equals("comment_is_public") ||
                    action.field.equals("comment_includes_word") ||
                    action.field.equals("ticket_form_id")
            ) {
                conditions.add(action);
            }
            if (action.operator.equals("not_changed") ||
                    action.operator.equals("not_value_previous") ||
                    action.operator.equals("changed") ||
                    action.operator.equals("value_previous")) {
                conditions.add(action);
            }
        }
        for (var action : conditions) {
            trigger.conditions.all.remove(action);
        }
    }

    private static void CreateAndASaveTickets() throws Exception {
        try {
            LoadData();
            SaveMigrationProgress();
            File folder = new File("/Users/tnluser/Downloads/untitled folder 5/data/June");
            File[] listOfFiles = folder.listFiles();
            try {
                for (final File o : listOfFiles) {
                    try {
                        if (o.getPath().toString().contains(".DS_Store") == false) {
                            ProcessFile(o.getPath().toString());
                        }
                        System.out.println("File Processing Completed " + o.getPath().toString());
                    } catch (Exception e) {
                        System.out.println("File Processing failed " + o.getPath().toString());
                    }
                }
            } finally {
                System.out.println("All Done");
                //exec.shutdown();
            }
            String filePath = "/Users/tnluser/Downloads/export-2023-05-31-1000-11306918-157368105115057380.json";

            //System.out.println("All Done");
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void ProcessFile(String filePath) throws Exception {


        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                try {
                    ProcessLine(line);
                    line = reader.readLine();
                } catch (Exception ex) {
                    System.out.println(ex.toString() + ex.getStackTrace());
                    throw ex;
                }
            }
            reader.close();
        }catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }



//        Files.lines(Path.of(filePath))
//                .parallel()
//                .forEach(line -> {
//                    try {
//                        ProcessLine(line);
//
//                    } catch (Exception ex) {
//                        System.out.println(ex.toString() + ex.getStackTrace());
//                    }
//                });
//        SaveMigrationProgress();
    }

    static int t=0;
    private static void ProcessLine(String line) throws Exception {
        Root.Ticket ticket = null;
        try {
            ticket = gson.fromJson(line, Root.Ticket.class);
            if (migratedTicketInfo.containsKey(ticket.id) == false) {
                migratedTicketInfo.put(ticket.id, new MigratedTicketInfo());
                migratedTicketInfo.get(ticket.id).zendeskTicketId = ticket.id;
            }
            if(migratedTicketInfo.get(ticket.id).Error ==null)
            {
                migratedTicketInfo.get(ticket.id).Error = new MigratedTicketInfo.Error();
            }
            if (mapFormsCategory.containsKey(String.valueOf(ticket.ticket_form_id)) == false) {
                migratedTicketInfo.get(ticket.id).Error.TicketError = "NotRequiredForm-" + ticket.ticket_form_id;
            } else if (ticket.group == null) {
                migratedTicketInfo.get(ticket.id).Error.TicketError = "NotRequiredGroup-Null";
            } else if (projectGroups.containsKey(ticket.group.name.trim()) == false) {
                migratedTicketInfo.get(ticket.id).Error.TicketError = "NotRequiredGroup-" + ticket.group.name.trim();
            } else {
                Root.Ticket finalTicket = ticket;
                exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (migratedTicketInfo.get(finalTicket.id).wfmsTicketId == null) {
                                ProcessLines(finalTicket);
                            }
                            UploadMissingAttachments(finalTicket);
                            if (migratedTicketInfo.get(finalTicket.id).comments.size() != finalTicket.comments.size()) {
                                if (migratedTicketInfo.get(finalTicket.id).wfmsTicketId != null) {
                                    AddMissingComments(finalTicket);
                                }
                            }
                            if (migratedTicketInfo.get(finalTicket.id).Error.CommentError.equals("") &&
                                    migratedTicketInfo.get(finalTicket.id).Error.TicketCreateError.equals("") &&
                                    migratedTicketInfo.get(finalTicket.id).Error.TicketUpdateError.equals("") &&
                                    migratedTicketInfo.get(finalTicket.id).Error.TicketError.equals("")) {

                            } else {
                                System.out.println(migratedTicketInfo.get(finalTicket.id).Error.CommentError + migratedTicketInfo.get(finalTicket.id).Error.TicketCreateError +
                                        migratedTicketInfo.get(finalTicket.id).Error.TicketUpdateError + migratedTicketInfo.get(finalTicket.id).Error.TicketError);
                                System.out.println(t++);
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }
    private static void SaveMigrationProgress() throws Exception {
        Runnable helloRunnable = new Runnable() {
            public void run() {
                try {
                    synchronized (lock) {
                        SaveFile(GetCompiledFilePath("MigratedTicketInfo"), migratedTicketInfo.values());

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 30, TimeUnit.SECONDS);
    }

    private static void AddMissingComments(Root.Ticket ticket) throws Exception {

        ArrayList<WFMSObjects.CreateConversationRequest> comments = new ArrayList<WFMSObjects.CreateConversationRequest>();
        if (ticket.comments != null) {
            for (var item : ticket.comments) {
                if(migratedTicketInfo.get(ticket.id).comments.containsKey(item.id))
                {
                    continue;
                }
                var comment = new WFMSObjects.CreateConversationRequest();
                comments.add(comment);
                comment.WfmsID   = item.id;
                if (users.containsKey(item.author_id)) {
                    comment.author = users.get(item.author_id).name;
                    comment.from = users.get(item.author_id).email;
                } else {
                    comment.author = item.author_id;
                    comment.from = item.author_id;
                    System.out.println("Weird User found  " + item.author_id);
                }
                comment.authorDetails = new WFMSObjects.CreateConversationRequest.User();
                comment.authorDetails.email = comment.from;
                comment.authorDetails.name = comment.author;
                comment.authorDetails.roles = new String[]{"requestor"};
                if (comment.from == null) {
                    comment.from = "";
                }
                comment.to = new ArrayList<>();
                comment.cc = new ArrayList<>();
                comment.createdAt = item.created_at;
                comment.updatedAt = item.created_at;
                if (item.via != null && item.via.source != null && item.via.source.to != null) {
                    comment.to.add(item.via.source.to.address);
                }
                if (item.via != null && item.via.source != null && item.via.source.to != null && item.via.source.to.email_ccs != null) {
                    for (var ccs : item.via.source.to.email_ccs) {
                        if (users.containsKey(ccs)) {
                            comment.cc.add(users.get(ccs).email);
                        } else if (ccs.contains("@")) {
                            comment.cc.add(ccs);
                        }
                        else {
                            comment.cc.add(ccs);
                            System.out.println("Weird User found 2  " + ccs);

                        }
                    }
                }
                comment.text = item.html_body;
                if (item.type.equals("comment")) {
                    comment.type = "reply";
                } else {
                    comment.type = "note";
                }
                if (item.via.channel.equals("web")) {
                    comment.source = "web-form";
                } else {
                    comment.source = "mail";
                }
                comment.attachments = new ArrayList<>();
                for (var attachment : item.attachments) {
                    if(migratedTicketInfo.get(ticket.id).attachments.containsKey(attachment.id)) {
                        comment.attachments.add(migratedTicketInfo.get(ticket.id).attachments.get(attachment.id));
                    }
                }
            }
        }

        for (var comment : comments) {
            String response = SaveItemsInWFMS("wfms-ticket-service/api/v1/tickets/" + migratedTicketInfo.get(ticket.id).wfmsTicketId + "/conversations", comment, migratedTicketInfo.get(ticket.id).projectId);
            if (response.contains("created successfully") == false) {
                migratedTicketInfo.get(ticket.id).Error.CommentError = response;
                System.out.println("comment error " + response);
            } else {
                String replyId = (new Gson()).fromJson(response, WFMSObjects.Response.class).data.replyId;
                migratedTicketInfo.get(ticket.id).comments.put(comment.WfmsID, replyId);
            }
        }

    }

    @NotNull
    private static void LoadData() throws Exception {

        System.out.println("Loading data");
        triggers = FileHelper.GetData(
                GetFilePath("triggers"),
                new TypeToken<ArrayList<Root.Trigger>>() {
                }.getType());

         wfmsTriggers   = FileHelper.GetData(
                GetCompiledFilePath("triggers"),
                new TypeToken<ArrayList<Root.Trigger>>() {
                }.getType());

         if(users==null ) {
             users = FileHelper.GetData(
                     GetFilePath("users"),
                     new TypeToken<ArrayList<Root.User>>() {
                     }.getType());
         }

        group_memberships = FileHelper.GetData(
                GetFilePath("group_memberships"),
                new TypeToken<ArrayList<Root.GroupMembership>>() {
                }.getType());
        groups = FileHelper.GetData(
                GetFilePath("groups"),
                new TypeToken<ArrayList<Root.Group>>() {
                }.getType());

        wfmsGroups = FileHelper.GetData(
                GetCompiledFilePath("Groups"),
                new TypeToken<ArrayList<WFMSObjects.Group>>() {
                }.getType());

        wfmsProjects = FileHelper.GetData(
                GetCompiledFilePath("Projects"),
                new TypeToken<ArrayList<WFMSObjects.Project>>() {
                }.getType());

        forms = FileHelper.GetData(
                GetFilePath("ticket_forms"),
                new TypeToken<ArrayList<Root.TicketForm>>() {
                }.getType());

        wfmsForms = FileHelper.GetData(
                GetCompiledFilePath("Forms"),
                new TypeToken<ArrayList<WFMSObjects.Form>>() {
                }.getType());

        migratedTicketInfo= FileHelper.GetConcurrentData(
                GetCompiledFilePath("MigratedTicketInfo"),
                new TypeToken<ArrayList<MigratedTicketInfo>>() {
                }.getType());

        if (Files.exists(Path.of(GetCompiledFilePath("ProjectGroups")))) {
            projectGroups = (new Gson()).fromJson(Files.readString(Path.of(GetCompiledFilePath("ProjectGroups"))), new TypeToken<Map<String, String>>() {
            }.getType());
        }

        mapFormsCategory = new HashMap<String, Integer>();
        for (var form : forms.values()) {
            for (var project : wfmsProjects.values()) {
                if (Arrays.stream(project.description.split(" ~ ")).toList().contains(form.name.trim())) {
                    mapFormsCategory.put(form.id, project.projectId);
                }
            }
        }

        fields = FileHelper.GetData(
                GetFilePath("ticket_fields"),
                new TypeToken<ArrayList<Root.TicketField>>() {
                }.getType());

        unassignedGroups = new HashMap<>();
        for (var item : wfmsGroups.values()) {
            if (item.groupName.startsWith("Unassigned")) {
                unassignedGroups.put(item.projectId, item.groupName);
            }
        }
    }

    private static void ProcessLines(Root.Ticket ticket) throws Exception {

        WFMSObjects.Ticket wfmsTicket = CreateTicket(ticket);
        String response = SaveItemsInWFMS("wfms-ticket-service/api/v1/tickets/migration", wfmsTicket, String.valueOf(wfmsTicket.projectId));
        if (response.contains("ticket created successfully") == false) {
            migratedTicketInfo.get(ticket.id).Error.TicketCreateError = response;
            System.out.println("ticket create error " + response);
            //throw new Exception("hello" + response);
        } else {
            String ticketId = (new Gson()).fromJson(response, WFMSObjects.Response.class).data.ticketId;
            WFMSObjects.TicketUpdate wfmsUpdateTicket = CreateUpdateTicket(ticket, wfmsTicket.projectId);
            String response2 = SaveItemsInWFMS("wfms-ticket-service/api/v1/tickets/" + ticketId, wfmsUpdateTicket, String.valueOf(wfmsTicket.projectId), "PUT");
            if (response2.contains("ticket updated successfully") == false) {
                if (response2.contains("CustomValidatorException: Agent Validation Failed")) {
                    migratedTicketInfo.get(ticket.id).Error.TicketUpdateError = response2;
                    //System.out.println("ticket update error " + response2);
                } else if (response2.contains("CustomValidatorException: Agent.Email is Blank")) {
                    migratedTicketInfo.get(ticket.id).Error.TicketUpdateError = response2;
                    //System.out.println("ticket update error " + response2);
                } else {
                    migratedTicketInfo.get(ticket.id).Error.TicketUpdateError = response2;
                    System.out.println("ticket update error " + response2);
                    //throw new Exception("hello hello" + response);
                }
            } else {
                var info = new MigratedTicketInfo();
                migratedTicketInfo.get(ticket.id).wfmsTicketId = ticketId;
                migratedTicketInfo.get(ticket.id).projectId = String.valueOf(wfmsTicket.projectId);
                System.out.println("Ticket Saved " + i++);
            }
        }
        //SaveMigrationProgress();
    }

    private static void AddUser(HashMap<String, Root.User> users, String authorId) throws Exception {
        try {

            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("https://byjusites.zendesk.com/api/v2/users/" + authorId + ".json")
                    .method("GET", null)
                    .addHeader("Authorization", "Basic YXJ2aW5kLm5hcmF5YW5hbjFAYnlqdXMuY29tL3Rva2VuOkJwWGxOWktzS2ZMTEFvTVlCUzFqWUJhb3k5VFljdFhzWDMzOWZqVUw=")
                    .addHeader("Cookie", "__cfruid=6ce77b34a43e5ab38ec4e6e5c4eaa2605203b9d8-1678770988; _zendesk_cookie=BAhJIhl7ImRldmljZV90b2tlbnMiOnt9fQY6BkVU--459ed01949a36415c1716b5711271c3d08918307; __cfruid=c7ebf2d7bb79c27d5d8764470d8b2b3f18e52528-1684165849; _zendesk_cookie=BAhJIhl7ImRldmljZV90b2tlbnMiOnt9fQY6BkVU--459ed01949a36415c1716b5711271c3d08918307; _zendesk_session=ekZvd1JHV2Z5MS9xZ1BmYmcrS1V6di9UQVM5RU9pejhYbnZ6d3dTSTdSai9sSmppcnZ5RysxaS9sZUZYdDJCSENhN25EQVB2cDVHZzNMeXRVK2gyaWRUQk03VnFNeWtKVmt6KzEya0M4TkNmOVRDM2gzRGdVNU5MVHZkUFpDamExVVRUVDNMMFFGOW96WHZvYVNEM2VJa0xQdzZlRnpQR2dqL3E5dlVsL3JDSmlWQm9yakR3MFZRNUJUT3VVYW8xSXNacHdzZVhtTTZtZkc3T21lMU5mT3pSMVlveDgzS0ZKN01rYkFrWHBpMD0tLVJheWkrVzR1OXF6Q1I5WGhQd3VJcWc9PQ%3D%3D--589b142c76594d82fd68e0b65e7f5584bd0c0a0c")
                    .build();
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Root root = null;
            try {
                 root = new Gson().fromJson(json, Root.class);
            } catch (JsonSyntaxException ex){
                System.out.println(ex.toString() + ex.getStackTrace());
                throw ex;
            }
            if (root.user == null && authorId.contains("@") == false && authorId.matches("-?\\d+(\\.\\d+)?") && authorId.equals("-1") == false) {
                throw new Exception();
            } else if (root.user != null) {
                synchronized (lock) {
                    users.put(authorId, root.user);
                    //SaveFile(GetFilePath("users"), users.values());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    @NotNull
    private static HashMap<Long, String> UploadMissingAttachments(Root.Ticket ticket) throws Exception {
        ArrayList<Root.Attachment> commentAttachments = new ArrayList<Root.Attachment>();
        if (ticket.comments != null) {
            for (var item : ticket.comments) {
                commentAttachments.addAll(item.attachments);
            }
        }
        HashMap<Long, String> attachments = new HashMap<>();

        String directoryPath = DownloadedDataPath + ticket.id;



        boolean anyactiontake = false;
        for (var attachment : commentAttachments) {
            if (migratedTicketInfo.get(ticket.id).attachments.containsKey(attachment.id) == false) {
                var directory = new File(directoryPath);
                if (anyactiontake == false && directory.exists() == false) {
                    directory.mkdirs();
                }
                anyactiontake = true;
                String attachmentId = DownloadUploadAttachment(attachment.content_url, attachment.file_name, directoryPath,0);

                migratedTicketInfo.get(ticket.id).attachments.put(attachment.id, attachmentId);

            }
        }
        if (anyactiontake) {
            System.out.println("File Attachment Completed  " + commentAttachments.size());
            FileUtils.deleteDirectory(new File(directoryPath));
        }

        return attachments;
    }


    private static String DownloadUploadAttachment(String contentUrl, String fileName, String directoryPath, int retry) throws Exception {
        try {
            String filePath = directoryPath + "/" + fileName;
            var file = new File(filePath);
            if (file.exists() == false) {
                downloadfile(contentUrl, file);
            }

            if (Files.size(Path.of(filePath)) > 4900000) {
                return "";
            }
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("image/png"), file))
                    .build();

            Request request = new Request.Builder()
                    .url(Endpoint + "wfms-ticket-service/api/v1/attachments")
                    .method("POST", body)
                    .addHeader("authority", "h-stage-apigateway.byjus.onl")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
                    .addHeader("origin", "https://workflow-managment-system-devtest.byjusweb.com")
                    .addHeader("projectid", "99906")
                    .addHeader("referer", "https://workflow-managment-system-devtest.byjusweb.com/")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"macOS\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "cross-site")
                    .addHeader("sessiontoken", SessionToken)
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .build();
            try {
                Response responseObject = client.newCall(request).execute();
                String response = responseObject.body().string();
                if (response.contains("created successfully")) {
                    System.out.println("Downloaded");
                    return (new Gson()).fromJson(response, WFMSObjects.Response.class).data.id;
                } else if (retry < 5) {
                    return DownloadUploadAttachment(contentUrl, fileName, directoryPath, retry++);
                }
                else {
                    throw new Exception(response);
                }
            } catch (SocketTimeoutException ex) {
                System.out.println("SocketTimeout...Retying");
                Thread.sleep(5 * 1000);
                System.out.println("Retying now");
                if (retry < 5) {
                    return DownloadUploadAttachment(contentUrl, fileName, directoryPath, retry++);
                }
                    throw ex;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void downloadfile(String contentUrl, File file) {
        try {
            FileUtils.copyURLToFile(new URL(contentUrl), file);
        } catch (ConnectException ex) {
            downloadfile(contentUrl, file);

        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            downloadfile(contentUrl, file);
        }
    }
/*
    private static String getToken() {
        long l = 999999;
        String SERVICE_TOKEN = "ee924d01d342ed0156e0d99e8ea96368099464c2617b901595cd57207a6b349d";
        String JWT_KEY = "1e5d9dde1d6c6ec5372475b88f642618268ef8f65060a1780d63ce1124305a0f";
        return (new JwtService(l)).encrypt(JwtServiceEncryptRequest.<String>builder().data(SERVICE_TOKEN).secretKey(JWT_KEY).build());
    }

 */

    @Nullable
    private static WFMSObjects.Ticket CreateTicket(Root.Ticket ticket) throws Exception {
        WFMSObjects.Ticket wfmsTicket = new WFMSObjects.Ticket();
        if (mapFormsCategory.containsKey(String.valueOf(ticket.ticket_form_id))) {
            wfmsTicket.category = wfmsProjects.get(String.valueOf(mapFormsCategory.get(String.valueOf(ticket.ticket_form_id)))).name;
            wfmsTicket.projectId = mapFormsCategory.get(String.valueOf(ticket.ticket_form_id));
        } else if (ticket.group != null && groups.containsKey(ticket.group.name.trim())) {
            wfmsTicket.category = wfmsProjects.get(String.valueOf(groups.get(ticket.group.name.trim()).projectId)).name;
            wfmsTicket.projectId = groups.get(ticket.group.name.trim()).projectId;
        } else {
            return null;
        }

        wfmsTicket.requester = new WFMSObjects.Ticket.Requester();
        wfmsTicket.requester.email = ticket.requester.email;
        wfmsTicket.requester.name = ticket.requester.name;

        if (ticket.via.channel.equals("api")) {
            wfmsTicket.channel = "Zendesk-API";
        } else if (ticket.via.channel.equals("email")) {
            wfmsTicket.channel = "Zendesk-Mail";
        } else if (ticket.via.channel.equals("web")) {
            wfmsTicket.channel = "Zendesk-Portal";
        } else {
            wfmsTicket.channel = "Zendesk-" + ticket.via.channel;
        }

        wfmsTicket.details = new HashMap<>();
        wfmsTicket.details.put("ZendeskTicket", "https://byjusites.zendesk.com/agent/tickets/" + ticket.id);
        if (ticket.subject.trim().equals("")) {
            ticket.subject = "Empty Subject Migrated from Zendesk";
        }
        wfmsTicket.details.put("subject", ticket.subject);
        wfmsTicket.details.put("description", ticket.description);
        wfmsTicket.details.put("contactnumber", ticket.requester.phone);
        wfmsTicket.details.put("customerName", ticket.requester.name);

        if (ticket.requester.email == null) {
            wfmsTicket.details.put("customerEmail", "aman.seth@byjus.com");
        } else {
            wfmsTicket.details.put("customerEmail", ticket.requester.email);
        }

        wfmsTicket.details.put("subCategory", forms.get(String.valueOf(ticket.ticket_form_id)).name.trim());
        AddFields(fields, ticket, wfmsTicket);

        if (wfmsTicket.details.get("issueType").equals("")) {
            wfmsTicket.details.put("issueType", "NotSelected");
        }
        if (wfmsTicket.details.get("issueSubType").equals("")) {
            wfmsTicket.details.put("issueSubType", "NotSelected");
        }
        wfmsTicket.createdAt = ticket.created_at;
        wfmsTicket.updatedAt = ticket.updated_at;
        return wfmsTicket;
    }

    @NotNull
    private static WFMSObjects.TicketUpdate CreateUpdateTicket(Root.Ticket ticket, int projectId) {
        WFMSObjects.TicketUpdate wfmsUpdateTicket = new WFMSObjects.TicketUpdate();

        if (ticket.group != null && wfmsGroups.containsKey(ticket.group.name.trim()) && wfmsGroups.get(ticket.group.name.trim()).projectId== projectId) {
            wfmsUpdateTicket.group = wfmsGroups.get(ticket.group.name.trim());
        } else {
            if (ticket.group != null ) {
                System.out.println("wrong group   " + ticket.group.name);
            }
            else {
                System.out.println("wrong group   no name" );
            }
            wfmsUpdateTicket.group = wfmsGroups.get(unassignedGroups.get(projectId));
        }
        wfmsUpdateTicket.agent = new WFMSObjects.TicketUpdate.Agent();
        if (ticket.assignee != null) {
            FillNames(ticket.assignee.name, wfmsUpdateTicket.agent);
            wfmsUpdateTicket.agent.email = ticket.assignee.email;
        }
        if(wfmsUpdateTicket.agent.email==null||wfmsUpdateTicket.agent.email.equals(""))
        {
            wfmsUpdateTicket.agent = null;
        }

        wfmsUpdateTicket.priority = new WFMSObjects.TicketUpdate.Priority();

        if (ticket.priority == null) {
            ticket.priority = "low";
        }
        switch (ticket.priority) {
            case "urgent":
                wfmsUpdateTicket.priority.key = "high";
                wfmsUpdateTicket.priority.value = "High";
                wfmsUpdateTicket.priority.priority = 100;
                break;
            case "high":
                wfmsUpdateTicket.priority.key = "medium";
                wfmsUpdateTicket.priority.value = "Medium";
                wfmsUpdateTicket.priority.priority = 1000;
                break;
            case "normal", "low":
                wfmsUpdateTicket.priority.key = "low";
                wfmsUpdateTicket.priority.value = "Low";
                wfmsUpdateTicket.priority.priority = 2000;
                break;
            default:
                wfmsUpdateTicket.priority.key = "low";
                wfmsUpdateTicket.priority.value = "Low";
                wfmsUpdateTicket.priority.priority = 2000;
                break;
        }
        String ss = ticket.priority;
        wfmsUpdateTicket.updatedBy = new WFMSObjects.TicketUpdate.UpdatedBy();
        wfmsUpdateTicket.updatedBy.email = "aman.seth@byjus.com";
        wfmsUpdateTicket.updatedBy.name = "Aman Seth";
        wfmsUpdateTicket.dueDate = ticket.due_at;
        wfmsUpdateTicket.status = new WFMSObjects.TicketUpdate.Status();

        switch (ticket.status) {

            case "open":
                wfmsUpdateTicket.status.key = "open";
                wfmsUpdateTicket.status.value = "OPEN";
                wfmsUpdateTicket.status.priority = 1;
                break;
            case "closed":
                wfmsUpdateTicket.status.key = "close";
                wfmsUpdateTicket.status.value = "CLOSE";
                wfmsUpdateTicket.status.priority = 2;
                break;
            case "resolved":
                wfmsUpdateTicket.status.key = "resolved";
                wfmsUpdateTicket.status.value = "RESOLVED";
                wfmsUpdateTicket.status.priority = 3;
                break;
            case "hold", "pending":
                wfmsUpdateTicket.status.key = "pending";
                wfmsUpdateTicket.status.value = "PENDING";
                wfmsUpdateTicket.status.priority = 4;
                break;
            case "new":
                wfmsUpdateTicket.status.key = "new";
                wfmsUpdateTicket.status.value = "NEW";
                wfmsUpdateTicket.status.priority = 5;
                break;
            default:
                wfmsUpdateTicket.status.key = "new";
                wfmsUpdateTicket.status.value = "NEW";
                wfmsUpdateTicket.status.priority = 5;
                break;
        }
        return wfmsUpdateTicket;
    }

    private static void AddFields(HashMap<String, Root.TicketField> fields, Root.Ticket ticket, WFMSObjects.Ticket t) throws Exception {
        for (var field : ticket.fields) {
            String fieldValue = null;
            if (field.value != null && (field.value.getClass().equals(String.class) || field.value.getClass().equals(Boolean.class))) {
                fieldValue = field.value.toString();
            } else if (field.value != null && field.value.getClass().equals(ArrayList.class)) {
                fieldValue = String.join(",", (ArrayList) field.value);
            } else if (field.value != null) {
                throw new Exception();
            }

            if (fields.containsKey(field.id) && fields.get(field.id).custom_field_options != null) {
                for (var option : fields.get(field.id).custom_field_options) {
                    if (option.value.equals(fieldValue)) {
                        fieldValue = option.name;
                        break;
                    }
                }
            }


            if (fields.containsKey(field.id) && issueType.contains(fields.get(field.id).title)) {
                if (t.details.containsKey("issueType") == false || t.details.get("issueType") == null) {
                    t.details.put("issueType", fieldValue);
                }
            } else if (fields.containsKey(field.id) && issueSubType.contains(fields.get(field.id).title)) {
                if (t.details.containsKey("issueSubType") == false || t.details.get("issueSubType") == null) {
                    t.details.put("issueSubType", fieldValue);
                }
            } else if (field.value != null && fields.containsKey(field.id)) {
                t.details.put(getFieldKey(fields.get(field.id).title), fieldValue);
            }
        }

        for (var field : ticket.custom_fields) {
            String fieldValue = "";
            if (field.value != null && (field.value.getClass().equals(String.class) || field.value.getClass().equals(Boolean.class))) {
                fieldValue = field.value.toString();
            } else if (field.value != null && field.value.getClass().equals(ArrayList.class)) {
                fieldValue = String.join(",", (ArrayList) field.value);
            } else if (field.value != null) {
                throw new Exception();
            }


            if (fields.containsKey(field.id) && fields.get(field.id).custom_field_options != null) {
                for (var option : fields.get(field.id).custom_field_options) {
                    if (option.value.equals(fieldValue)) {
                        fieldValue = option.name;
                        break;
                    }
                }

            }

            if (fields.containsKey(field.id) && issueType.contains(fields.get(field.id).title)) {
                if (t.details.containsKey("issueType") == false || t.details.get("issueType") == null) {
                    t.details.put("issueType", fieldValue);
                }
            } else if (fields.containsKey(field.id) && issueSubType.contains(fields.get(field.id).title)) {
                if (t.details.containsKey("issueSubType") == false || t.details.get("issueSubType") == null) {
                    t.details.put("issueSubType", fieldValue);
                }
            } else if (field.value != null && fields.containsKey(field.id)) {
                String fieldKey = getFieldKey(fields.get(field.id).title);
                if (t.details.containsKey(fieldKey) == false || t.details.get(fieldKey) == null) {
                    t.details.put(fieldKey, fieldValue);
                }
            }
        }
    }

    private static void AddData() {
        issueType.add("Incident Type");
        issueType.add("City - BTC");
        issueType.add("Category - IT Desk");
        issueType.add("Category | SF-CR");
        issueType.add("Select Site Type");
        issueType.add("User Impacted");
        issueType.add("Sub Category - PD Uploads");
        issueType.add("Digital Finance Types");
        issueType.add("Request Type");
        issueType.add("STC - Type");
        issueType.add("Folder");
        issueType.add("Source");
        issueSubType.add("Calling Type - Issue");
        issueSubType.add("Calling Type - Requests");
        issueSubType.add("Issue Type - BTC");
        issueSubType.add("Issue Category - Network");
        issueSubType.add("Request Type - Media");
        issueSubType.add("Issue Category - Email");
        issueSubType.add("Issue Sub Category - Desktop");
        issueSubType.add("Issue Sub Category - Projector/Printer");
        issueSubType.add("Issue Category - Application");
        issueSubType.add("Sub-Category | Request | SF-CR");
        issueSubType.add("Sub-Category | Issue | SF-CR");
        issueSubType.add("Sub-Category | Query | SF-CR");
        issueSubType.add("Attribution - INC");
        issueSubType.add("Affected Infrastructure");
        issueSubType.add("Project Name - PD Uploads");
        issueSubType.add("Loan Verification Types");
        issueSubType.add("Ticket Source");
        issueSubType.add("Team Heads");
        issueSubType.add("STC - Tags 1");
        issueSubType.add("Category");
        issueSubType.add("Assign to Group");
    }

    private static void CreateUsersCSV() {
        try {
            LoadData();
            String s = "";
            HashMap<String, ArrayList<WFMSObjects.Group.Agent>> wfmsUsers = new HashMap<>();
            for (var group : wfmsGroups.values()) {
                String projectName = wfmsProjects.get(String.valueOf(group.projectId)).name;
                if (wfmsUsers.containsKey(projectName)) {
                    wfmsUsers.get(projectName).addAll(group.agent);
                } else {
                    wfmsUsers.put(projectName, group.agent);
                }
            }

            for (var project : wfmsUsers.keySet()) {
                for (var user : wfmsUsers.get(project)) {
                    s = s + project + "," + user.firstName + "," + user.lastName + "," + user.email + ",";
                    if (user.email != null) {
                        for (var zendeskUser : users.values()) {
                            if (user.email.equals(zendeskUser.email)) {
                                s = s + zendeskUser.phone + ",";
                                s = s + zendeskUser.role;
                                break;
                            }
                        }
                    }
                    s = s + "\n";
                }
            }
            s = s;
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());

        }
    }

    private static void FixProjectIds(HashMap<String, WFMSObjects.Group> groups) throws Exception {
        try {

            LoadData();

            HashMap<String, Integer> projectss = new HashMap<>();
            for (var project : wfmsProjects.values()) {
                projectss.put(project.name, project.projectId);
            }
            var list = new ArrayList<>();
            for (var group : groups.values()) {
                if (projectGroups.containsKey(group.groupName) == false) {
                    list.add(group.groupName);
                } else if (projectss.containsKey(projectGroups.get(group.groupName) + ProjectPrefix)) {
                    group.projectId = projectss.get(projectGroups.get(group.groupName) + ProjectPrefix);
                } else {
                    System.out.println("Group not found : " + projectGroups.get(group.groupName));
                }
            }
            for (var group : list) {
                groups.remove(group);
            }
            for (var project : wfmsProjects.values()) {
                WFMSObjects.Group group = new WFMSObjects.Group();
                group.projectId = project.projectId;
                group.groupName = getUnassignedGroupName(project.name);
                group.description = getUnassignedGroupName(project.name);
                group.ownerEmail = "aman.seth@byjus.com";
                group.groupEmail = "";
                HashSet<String> agentEmailAddress  = new HashSet<>();
                group.agent = new ArrayList<>();
                for(var v : groups.values()) {
                    if(v.projectId==project.projectId) {
                        for (var agent : v.agent) {
                            if (agentEmailAddress.contains(agent.email) == false) {
                                group.agent.add(agent);
                                agentEmailAddress.add(agent.email);
                            }
                        }
                    }
                }
                groups.put(group.groupName, group);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    @NotNull
    private static String getUnassignedGroupName(String projectName) {
        return "Unassigned tickets of - " + projectName;
    }

    private static void DownloadDate() throws Exception {
        for (Map.Entry<String, String> item : dictDataToDownload.entrySet()) {
            DownloadData(item.getKey(), item.getValue());
        }
        //DownloadUsers();
    }

    private static void ModifyData() throws Exception {
        AssignUsersToGroups();
        AddGroupEmail();
        ReplaceUserConditionsByAgentConditionsIfEmpty();
        RemoveRequiredOnStatusesFromForm();
        RemoveInactiveFieldsFromForm();
        RemoveInactiveFieldsFromConditions();

    }

    private static void RemoveRequiredOnStatusesFromForm() throws Exception {
        try {

            LoadData();

            for (var form : forms.values()) {
                for (var condition : form.end_user_conditions) {
                    for (var field : condition.child_fields) {
                        field.required_on_statuses = null;
                    }
                }
            }
            SaveFile(GetFilePath("ticket_forms"), forms.values());
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void RemoveInactiveFieldsFromConditions() throws Exception {
        try {

            LoadData();

            for (var form : forms.values()) {
                ArrayList<Root.TicketForm.FormCondition> lstCondition = new ArrayList<>();
                for (var condition : form.end_user_conditions) {
                    ArrayList<Root.TicketForm.FormCondition.ChildField> lst = new ArrayList<>();
                    for (var field : condition.child_fields) {
                        if (fields.containsKey(field.id) == false
                                || fields.get(field.id).active == false
                                || (fields.get(field.id).visible_in_portal == false && form.end_user_visible)) {
                            lst.add(field);
                        }
                    }
                    for (var field : lst) {
                        condition.child_fields.remove(field);
                    }
                    if (condition.child_fields.size() == 0) {
                        lstCondition.add(condition);
                    }
                }
                for (var condition : lstCondition) {
                    form.end_user_conditions.remove(condition);
                }
            }


            SaveFile(GetFilePath("ticket_forms"), forms.values());
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void RemoveInactiveFieldsFromForm() throws Exception {
        try {
            LoadData();
            for (var form : forms.values()) {
                ArrayList<String> lst = new ArrayList<>();
                for (var field : form.ticket_field_ids) {
                    if (fields.containsKey(field) == false
                            || fields.get(field).active == false
                            || (fields.get(field).visible_in_portal == false && form.end_user_visible)) {
                        lst.add(field);
                    }
                }

                for (var field : lst) {
                    form.ticket_field_ids.remove(field);
                }
            }

            SaveFile(GetFilePath("ticket_forms"), forms.values());
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void ReplaceUserConditionsByAgentConditionsIfEmpty() throws Exception {
        try {

            LoadData();
            for (var form : forms.values()) {
                if (form.end_user_conditions.size() == 0) {
                    if (form.agent_conditions.size() != 0) {
                        form.end_user_conditions = (ArrayList<Root.TicketForm.FormCondition>) form.agent_conditions.clone();
                    }
                }
                form.agent_conditions.clear();
            }
            SaveFile(GetFilePath("ticket_forms"), forms.values());
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }

    }

    private static void GenerateWFMSModels() throws Exception {
        CreateZendeskGroups();
        CreateZendeskForms();
    }

    private static void SaveWFMSModels() throws Exception {
        try {
            //ModifyData();
            //GenerateWFMSModels();
            SaveInWFMS();
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void SaveInWFMS() throws Exception {
        SaveItemsInWFMS(wfmsProjects.values(), "wfms-configuration-management/wfms/projectManagement/v1/projectDetails");
        SaveItemsInWFMS(wfmsForms.values(), "wfms-configuration-management/wfms/configurationManagement/v1/projectConfiguration");
        SaveGroups();
    }

    private static void SaveGroups() throws Exception {
        try {
            for (var item : wfmsGroups.values()) {
                if(String.valueOf(item.projectId).equals( ProjectID)) {
                    String response = SaveItemsInWFMS("wfms-configuration-management/wfms/groupManagement/v1/groups", item, String.valueOf(item.projectId));
                    try {
                        item.groupId = (new Gson()).fromJson(response, WFMSObjects.Response.class).data.group.groupId;
                    } catch (Exception ex) {
                        System.out.println(ex.toString() + ex.getStackTrace());
                        //throw ex;
                    }
                }
            }
            SaveFile(GetCompiledFilePath("Groups"), wfmsGroups.values());
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }


    private static void CreateZendeskForms() throws Exception {
        try {

            ArrayList<WFMSObjects.Form> wfmsForms = new ArrayList<>();
            LoadData();
            for (var project : wfmsProjects.values()) {
                WFMSObjects.Form wfmsForm = new WFMSObjects.Form();
                wfmsForm.projectId = project.projectId;
                wfmsForm.versionNumber = 1;
                wfmsForm.projectName = project.name;
                wfmsForm.resourceType = "project";
                wfmsForm.inUse = true;
                var arr = project.description.split(" ~ ");
                WFMSObjects.Form.Configuration.Field wfmsfield = new WFMSObjects.Form.Configuration.Field();
                wfmsfield.fieldValue = "Sub Category";
                wfmsfield.fieldKey = "subCategory";

                wfmsfield.metadata = new WFMSObjects.Form.Configuration.Field.Metadata();
                wfmsfield.metadata.dataType = "String";
                wfmsfield.metadata.required = "true";
                wfmsfield.metadata.fieldType = "dropdown";
                wfmsfield.metadata.defaultValue = "";

                wfmsfield.data = new ArrayList<>();
                for (var f : arr) {
                    for (var form : forms.values()) {
                        if (f.equals(form.name.trim())) {
                            var configuration = GetFormConfiguration(form);

                            var data = new WFMSObjects.Form.Configuration.Field.Datum();
                            data.fields = configuration.fields;
                            data.dataValue = form.name;
                            wfmsfield.data.add(data);
                            break;
                        } else {
                            wfmsForm.inUse = true;
                        }
                    }
                }
                wfmsForm.configuration = new WFMSObjects.Form.Configuration();
                wfmsForm.configuration.fields = new ArrayList<>();
                wfmsForm.configuration.fields.add(wfmsfield);
                AddMandatoryFields(wfmsForm);
                wfmsForms.add(wfmsForm);
            }
            SaveFile(GetCompiledFilePath("Forms"), wfmsForms);
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static void AddMandatoryFields(WFMSObjects.Form wfmsForm) {
        var customerName = new WFMSObjects.Form.Configuration.Field();
        customerName.data = new ArrayList<>();
        customerName.data.add(new WFMSObjects.Form.Configuration.Field.Datum("customerName"));
        customerName.fieldKey = "customerName";
        customerName.fieldValue = "Customer Name";
        customerName.metadata = new WFMSObjects.Form.Configuration.Field.Metadata("", "String", "true", "text", "");
        wfmsForm.configuration.fields.add(customerName);

        var customerEmail = new WFMSObjects.Form.Configuration.Field();
        customerEmail.data = new ArrayList<>();
        customerEmail.data.add(new WFMSObjects.Form.Configuration.Field.Datum("customerEmail"));
        customerEmail.fieldKey = "customerEmail";
        customerEmail.fieldValue = "Customer Email";
        customerEmail.metadata = new WFMSObjects.Form.Configuration.Field.Metadata("/^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$/", "String", "true", "text", "");
        wfmsForm.configuration.fields.add(customerEmail);

        var subject = new WFMSObjects.Form.Configuration.Field();
        subject.data = new ArrayList<>();
        subject.data.add(new WFMSObjects.Form.Configuration.Field.Datum("enter subject"));
        subject.fieldKey = "subject";
        subject.fieldValue = "Subject";
        subject.metadata = new WFMSObjects.Form.Configuration.Field.Metadata("", "String", "true", "text", "");
        wfmsForm.configuration.fields.add(subject);

        var description = new WFMSObjects.Form.Configuration.Field();
        description.data = new ArrayList<>();
        description.data.add(new WFMSObjects.Form.Configuration.Field.Datum("enter description"));
        description.fieldKey = "description";
        description.fieldValue = "Description";
        description.metadata = new WFMSObjects.Form.Configuration.Field.Metadata("", "String", "true", "description", "");
        wfmsForm.configuration.fields.add(description);
    }

    private static WFMSObjects.Form.Configuration GetFormConfiguration(Root.TicketForm form) throws Exception {


        form.ticket_field_ids.remove("360026040818");
        form.ticket_field_ids.remove("360026040798");
        form.ticket_field_ids.remove("360026040918");
        form.ticket_field_ids.remove("360026040838");
        form.ticket_field_ids.remove("360026040898");
        form.ticket_field_ids.remove("9169849212561");

        for (var condition : form.end_user_conditions) {
            for (var childfield : condition.child_fields) {
                form.ticket_field_ids.remove(childfield.id);
            }
        }

        WFMSObjects.Form.Configuration configuration = new WFMSObjects.Form.Configuration();
        configuration.fields = new ArrayList<>();


        for (var field : form.ticket_field_ids) {
            WFMSObjects.Form.Configuration.Field wfmsfield = getField(form, fields, field);
            configuration.fields.add(wfmsfield);
        }
        return configuration;
    }

    @NotNull
    private static WFMSObjects.Form.Configuration.Field getField(Root.TicketForm form, HashMap<String, Root.TicketField> fields, String field) {

        WFMSObjects.Form.Configuration.Field wfmsfield = new WFMSObjects.Form.Configuration.Field();

        wfmsfield.fieldValue = fields.get(field).title;
        if(form.name.equals("Digital Finance")) {
            wfmsfield.fieldValue = fields.get(field).title;
        }

        if (issueType.contains(fields.get(field).title)) {
            wfmsfield.fieldKey = "issueType";
        } else if (issueSubType.contains(fields.get(field).title)) {
            wfmsfield.fieldKey = "issueSubType";
        } else {
            wfmsfield.fieldKey = getFieldKey(fields.get(field).title);
        }

        wfmsfield.metadata = new WFMSObjects.Form.Configuration.Field.Metadata();
        wfmsfield.metadata.regex = fields.get(field).regexp_for_validation;


        if (issueType.contains(fields.get(field).title)) {
            wfmsfield.metadata.required = String.valueOf(true);
        } else if (issueSubType.contains(fields.get(field).title)) {
            wfmsfield.metadata.required = String.valueOf(true);
        } else {
            wfmsfield.metadata.required = String.valueOf(fields.get(field).required);
        }

        //Hash
        // Map<String, WFMSObjects.Form.Configuration.Field> toHave = new HashMap<>();
        //HashMap<String, ArrayList<String>> toNotHave = new HashMap<>();
        //HashMap<String, ArrayList<String>> alreadyAdded = new HashMap<>();
        SetFieldType(fields, field, wfmsfield);
        wfmsfield.metadata.defaultValue = "";
        wfmsfield.data = new ArrayList<>();
        if (fields.get(field).type.equals("tagger") || fields.get(field).type.equals("multiselect")) {
            for (var fieldOption : fields.get(field).custom_field_options) {
                var data = new WFMSObjects.Form.Configuration.Field.Datum();
                data.dataValue = fieldOption.name;
                data.fields = new ArrayList<>();
                //alreadyAdded.put(data.dataValue, new ArrayList<>());
                //toNotHave.put(data.dataValue, new ArrayList<>());
                for (var condition : form.end_user_conditions) {
                    if (condition.parent_field_id.equals(field) && condition.value.equals(fieldOption.value)) {
                        for (var childfield : condition.child_fields) {
                            var subField = getField(form, fields, childfield.id);
                            data.fields.add(subField);
                            if (childfield.is_required) {
                                subField.metadata.required = String.valueOf(true);
                                //alreadyAdded.get(data.dataValue).add(subField.fieldKey);
                            } else {
                                /*toNotHave.get(data.dataValue).add(subField.fieldKey);
                                toHave.put(subField.fieldKey, subField);*/
                            }
                        }
                    }
                }
                wfmsfield.data.add(data);
            }
           /* for (var data :wfmsfield.data)
            {
                for(var newItem: toHave.values())
                {
                    if(toNotHave.containsKey(data.dataValue)==false ||  toNotHave.get(data.dataValue).contains(newItem.fieldKey)==false)
                    {
                      if(alreadyAdded.containsKey(data.dataValue)==false|| alreadyAdded.get(data.dataValue).contains(newItem.fieldKey)==false) {
                          data.fields.add(newItem);
                      }
                    }
                }
            }*/
        }
        return wfmsfield;
    }

    @NotNull
    private static String getFieldKey(String fieldTitle) {
        return fieldTitle.toLowerCase().replaceAll("[^a-z]", "");
    }

    private static void SetFieldType(HashMap<String, Root.TicketField> fields, String field, WFMSObjects.Form.Configuration.Field wfmsfield) {
        switch (fields.get(field).type) {
            case "tagger":
                wfmsfield.metadata.fieldType = "dropdown";
                wfmsfield.metadata.dataType = "String";
                break;
            case "text":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "checkbox":
                wfmsfield.metadata.fieldType = "checkbox";
                wfmsfield.metadata.dataType = "String";
                break;
            case "multiselect":
                wfmsfield.metadata.fieldType = "multiDropdown";
                wfmsfield.metadata.dataType = "String";
                break;
            case "regexp":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "textarea":
                wfmsfield.metadata.fieldType = "textArea";
                wfmsfield.metadata.dataType = "String";
                break;
            case "date":
                wfmsfield.metadata.fieldType = "datePicker";
                wfmsfield.metadata.dataType = "String";
                break;
            case "integer":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "assignee":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "custom_status":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "description":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "group":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "priority":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "status":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "subject":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            case "tickettype":
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
            default:
                wfmsfield.metadata.fieldType = "text";
                wfmsfield.metadata.dataType = "String";
                break;
        }
    }


    private static void CreateZendeskGroups() throws Exception {
        LoadData();


        HashMap<String, WFMSObjects.Group> wfmsGroups = new HashMap<>();
        for (var group : groups.values()) {
            WFMSObjects.Group wfmsGroup = new WFMSObjects.Group();
            if (group.name.equals("Inside Sales STC")) {
                wfmsGroup.groupName = group.name;
            }
            wfmsGroup.groupName = group.name;
            wfmsGroup.description = group.description;
            if (group.groupEmail != null) {
                wfmsGroup.groupEmail = String.join(",", group.groupEmail);
            } else {
                wfmsGroup.groupEmail = "";
            }
            wfmsGroup.agent = new ArrayList<>();
            group.ownerEmail = "";
            if (group.users != null) {
                for (var user : group.users) {
                    if (group.ownerEmail.equals("")) {
                        group.ownerEmail = user.email;
                    }
                    if (user.role.equals("admin")) {
                        group.ownerEmail = user.email;
                    }
                    WFMSObjects.Group.Agent agent = new WFMSObjects.Group.Agent();
                    agent.email = user.email;
                    FillNames(user.name, agent);
                    wfmsGroup.agent.add(agent);
                }
            }
            if (group.ownerEmail.equals("")) {
                group.ownerEmail = "aman.seth@byjus.com";
            }

            wfmsGroup.ownerEmail = group.ownerEmail;
            wfmsGroups.put(wfmsGroup.getId(), wfmsGroup);
        }
        FixProjectIds(wfmsGroups);
        SaveFile(GetFilePath("groups"), groups.values());
        SaveFile(GetCompiledFilePath("Groups"), wfmsGroups.values());
    }

    private static void FillNames(String userName, IName agent) {
        var name = userName.split(" ");
        agent.setFirstName(name[0]);
        agent.setLastName("");
        if (name.length > 1) {
            agent.setLastName(name[1]);
        }
    }



    private static void AssignUsersToGroups() throws Exception {

        LoadData();
        for (Root.Group item : groups.values()) {
            item.users = new HashSet<>();
        }
        for (var item : group_memberships.values()) {
            var groupmembership = item;
            if (groups.containsKey(groupmembership.group_id) && users.containsKey(groupmembership.user_id)) {
                groups.get(groupmembership.group_id).users.add(users.get(groupmembership.user_id));
            }
        }
        SaveFile(GetFilePath("groups"), groups.values());
    }

    private static void AddGroupEmail() throws Exception {

        LoadData();
        for (var item : groups.values()) {
            item.groupEmail = new HashSet<>();
        }

        for (var item : triggers.values()) {
            if (item.active) {
                String groupId = "";
                for (var action : item.actions) {
                    if (action.field.equals("group_id")) {
                        groupId = action.value.toString();
                    }
                }
                for (var condition : item.conditions.all) {
                    if (condition.field.equals("recipient")) {
                        if (groups.containsKey(groupId)) {
                            groups.get(groupId).groupEmail.add(condition.value.toString());
                        }
                    }
                }
                for (var condition : item.conditions.any) {
                    if (condition.field.equals("recipient")) {
                        if (groups.containsKey(groupId)) {
                            groups.get(groupId).groupEmail.add(condition.value.toString());
                        }
                    }
                }
            }
        }

        SaveFile(GetFilePath("groups"), groups.values());
    }

    private static void DownloadUsers() throws Exception {
        LoadData();

        ArrayList<ArrayList<String>> usersListList = new ArrayList<>();
        int i = 0;
        HashSet<String> userSet = new HashSet<>();
        for (var groupmembseship : group_memberships.values()) {
            String userId = ((Root.GroupMembership) groupmembseship).user_id;
            if (userSet.contains(userId)) {
                continue;
            }
            userSet.add(userId);
            if (i++ % 50 == 0) {
                usersListList.add(new ArrayList<>());
            }
            usersListList.get(usersListList.size() - 1).add(userId);
        }
        Root r = null;
        for (var userslist : usersListList) {
            r = DownloadData(
                    "users/show_many.json?ids=" + String.join(",", userslist),
                    "users", r);
        }
    }

    private static Root DownloadData(String endpoint, String propertyName) throws Exception {
        return DownloadData(endpoint, propertyName, null);
    }

    private static Root DownloadData(String endpoint, String propertyName, Root root) throws Exception {
        String json;
        endpoint = "https://byjusites.zendesk.com/api/v2/" + endpoint;
        try {
            do {
                json = GetJsonFromEndpoint(endpoint);
                try {
                    Root r = new Gson().fromJson(json, Root.class);

                    if (root != null) {
                        r.Merge(root, propertyName);
                    }
                    root = r;
                    endpoint = root.next_page;
//                    if (root.users.size() % 5000 == 0) {
//                        SaveFile(GetFilePath(propertyName), root.GetPropValue(propertyName));
//                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString() + ex.getStackTrace());
                    DownloadData(endpoint.replace("https://byjusites.zendesk.com/api/v2/",""), propertyName, root);
                    throw ex;
                }
            } while (endpoint != null);
            SaveFile(GetFilePath(propertyName), root.GetPropValue(propertyName));
            return root;
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static <T extends IWithID> void SaveItemsInWFMS(Collection<T> items, String endpoint) throws Exception {

        try {
            for (T item : items) {
                if (String.valueOf(item.getId()).equals(ProjectID)) {
                    SaveItemsInWFMS(endpoint, item, item.getId());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static <T> String SaveItemsInWFMS(String endpoint, T item, String projectid) throws Exception {
        return SaveItemsInWFMS(endpoint, item, projectid, "POST");
    }

    private static <T> String SaveItemsInWFMS(String endpoint, T item, String projectid,String methodType) throws Exception {
        return SaveItemsInWFMS(endpoint, item, projectid,methodType,0);
    }



    private static <T> String SaveItemsInWFMS(String endpoint, T item, String projectid, String methodType, int rerty) throws Exception {
        try {

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, gson.toJson(item));

            Request request = new Request.Builder()
                    .url(Endpoint + endpoint)
                    .method(methodType, body)
                    .addHeader("content-type", "application/json")
                    .addHeader("projectid", projectid)
                    .addHeader("authority", "h-stage-apigateway.byjus.onl")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
                    .addHeader("origin", "https://workflow-managment-system-staging.byjusweb.com")
                    .addHeader("referer", "https://workflow-managment-system-staging.byjusweb.com/")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"macOS\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "cross-site")
                    .addHeader("sessiontoken", SessionToken)
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .build();
            //System.out.println(endpoint + "   " + i);
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            //System.out.println(response);
            if (json.contains("Agent.Email is Blank") || json.contains("Agent Validation Failed") ) {
                return json;
            } else if (response.code() != 200 && response.code() != 201 && rerty < 5) {
                return SaveItemsInWFMS(endpoint, item, projectid, methodType, ++rerty);
            } else if (rerty >= 5) {
                System.out.println("***************" + response.code() + "***************" + json);
            }
            return json;
        } catch (SocketException ex) {
            if (rerty < 5) {
                return SaveItemsInWFMS(endpoint, item, projectid, methodType, ++rerty);
            } else {
                System.out.println(ex.toString() + ex.getStackTrace());
                return ex.toString();
            }
        } catch (SocketTimeoutException ex) {
            if (rerty < 5) {
                return SaveItemsInWFMS(endpoint, item, projectid, methodType, ++rerty);
            } else {
                System.out.println(ex.toString() + ex.getStackTrace());
                return ex.toString();
            }
        } catch (StreamResetException ex) {
            client = new OkHttpClient().newBuilder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build();

            if (rerty < 5) {
                return SaveItemsInWFMS(endpoint, item, projectid, methodType, ++rerty);
            } else {
                System.out.println(ex.toString() + ex.getStackTrace());
                return ex.toString();
            }
        } catch (IOException ex) {

            if (rerty < 5) {
                return SaveItemsInWFMS(endpoint, item, projectid, methodType, ++rerty);
            } else {
                System.out.println(ex.toString() + ex.getStackTrace());
                return ex.toString();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static String GetJsonFromEndpoint(String endpoint) {
        try {

            Request request = new Request.Builder()
                    .url(endpoint)
                    .method("GET", null)
                    .addHeader("Authorization", "Basic YXJ2aW5kLm5hcmF5YW5hbjFAYnlqdXMuY29tL3Rva2VuOkJwWGxOWktzS2ZMTEFvTVlCUzFqWUJhb3k5VFljdFhzWDMzOWZqVUw=")
                    .addHeader("Cookie", "__cfruid=6ce77b34a43e5ab38ec4e6e5c4eaa2605203b9d8-1678770988; _zendesk_cookie=BAhJIhl7ImRldmljZV90b2tlbnMiOnt9fQY6BkVU--459ed01949a36415c1716b5711271c3d08918307; __cfruid=25f1e929362acf47a45a240404ef905cd6fd057f-1681716863; _zendesk_cookie=BAhJIhl7ImRldmljZV90b2tlbnMiOnt9fQY6BkVU--459ed01949a36415c1716b5711271c3d08918307")
                    .addHeader("Accept", "*/*")
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();
            System.out.println("Calling for endpoint " + endpoint + " recieved");
            if (response.code() != 200) {
                System.out.println(response.code());
                Thread.sleep(60*1000);
                return GetJsonFromEndpoint(endpoint);
            }
            return json;
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            if (ex.getMessage().contains("429")) {
            } else {
                System.out.println("*******************************************************************************************");
            }
            return GetJsonFromEndpoint(endpoint);
        }
    }

    static Object obj = null;

    private static void SaveFile(String filePath, Object obj) throws Exception {
        try {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
            Files.writeString(Path.of(filePath), s);
        } catch (Exception ex) {
            System.out.println(ex.toString() + ex.getStackTrace());
            throw ex;
        }
    }

    private static String GetFilePath(String fileName) {
        return DownloadedDataPath + fileName + ".json";
    }

    private static String GetCompiledFilePath(String fileName) {
        return CompiledDataPath + fileName + ".json";
    }
}