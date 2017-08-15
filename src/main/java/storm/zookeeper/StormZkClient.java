package storm.zookeeper;

import org.apache.storm.cluster.*;
import org.apache.storm.generated.Assignment;
import org.apache.storm.generated.NimbusSummary;
import org.apache.storm.generated.StormBase;
import org.apache.storm.generated.SupervisorInfo;
import org.apache.storm.utils.Utils;
import org.apache.zookeeper.data.ACL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Storm objects are serialized before written to ZooKeeper which makes debugging a little bit difficult.
 * This class helps reading Storm objects from ZooKeeper.
 */
public class StormZkClient implements Runnable{
    private Map<String, Object> conf;
    private Map<String, Object> topologyConf;
    private IStateStorage stateStorage;
    private IStormClusterState _delegate;

    public StormZkClient() {
        try {
            conf = Utils.readStormConfig();
            topologyConf = Utils.readStormConfig();
            List<ACL> acls = Utils.getWorkerACL(topologyConf);
            stateStorage = ClusterUtils.mkStateStorage(conf, topologyConf, acls, new ClusterStateContext(DaemonType.NIMBUS));
            _delegate = ClusterUtils.mkStormClusterState(stateStorage, acls, new ClusterStateContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> assignments() {
        return _delegate.assignments(null);
    }

    public Assignment assignmentInfo(String stormId) {
        return _delegate.assignmentInfo(stormId, null);
    }

    public VersionedData<Assignment> assignmentInfoWithVersion(String stormId) {
        return _delegate.assignmentInfoWithVersion(stormId, null);
    }

    public List<NimbusSummary> nimbuses() {
        return _delegate.nimbuses();
    }

    public void printAllSupervisorInfo() {
        for (Map.Entry<String, SupervisorInfo> entry: _delegate.allSupervisorInfo().entrySet()) {
            System.out.println("Supervisor " + entry.getKey() + ": "+ entry.getValue());
        }
    }

    public void printAllNimbus() {
        for (NimbusSummary ns: nimbuses()) {
            System.out.println("Nimbus: " + ns);
        }
    }

    public void printAllTopologyBases() {
        for (Map.Entry<String, StormBase> entry: _delegate.topologyBases().entrySet()) {
            System.out.println("Topology: " + entry.getKey() + ": "+ entry.getValue());
        }
    }

    public void printAllTopologyAssignments() {
        for (Map.Entry<String, Assignment> entry: _delegate.topologyAssignments().entrySet()) {
            System.out.println("Topology: " + entry.getKey() + " : "+ entry.getValue());
        }
    }

    public void printAllAssignmentInfoWithVersion() {
        for (String topoId: assignments()) {
            VersionedData<Assignment> assignmentInfoWithVersion = assignmentInfoWithVersion(topoId);
            System.out.println("Version: " + assignmentInfoWithVersion.getVersion() + " : " + assignmentInfoWithVersion.getData());
        }

    }

    public void printUsage() {
        System.out.printf("Usage: [%s|%s|%s|%s|%s]\n", COMMAND_NIMBUS, COMMAND_SUPERVISOR,
                COMMAND_TOPOLOGY_BASE, COMMAND_ASSIGNMENT,COMMAND_ASSIGNMENT_VERSION);
    }

    private static final String COMMAND_NIMBUS = "nimbus";
    private static final String COMMAND_SUPERVISOR = "supervisor";
    private static final String COMMAND_TOPOLOGY_BASE = "stormbase";
    private static final String COMMAND_ASSIGNMENT = "assignment";
    private static final String COMMAND_ASSIGNMENT_VERSION = "assignment_version";

    public void process(String command) {
        command = command.toLowerCase();

        if (command.equals(COMMAND_NIMBUS)) {
            printAllNimbus();
        } else if (command.equals(COMMAND_SUPERVISOR)) {
            printAllSupervisorInfo();
        } else if (command.equals(COMMAND_ASSIGNMENT)) {
            printAllTopologyAssignments();
        } else if (command.equals(COMMAND_ASSIGNMENT_VERSION)) {
            printAllAssignmentInfoWithVersion();
        } else if (command.equals(COMMAND_TOPOLOGY_BASE)) {
            printAllTopologyBases();
        } else {
            System.out.println("Unknown command");
        }
    }

    public void run() {
        printUsage();

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        while(true) {
            String command = reader.next();
            process(command);
        }
    }

    public static void main(String[] args) {
        StormZkClient szClient = new StormZkClient();
        szClient.run();
    }
}
