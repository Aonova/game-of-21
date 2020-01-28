package source;

public class ProcessList //Arbitrary orginization class for keeping track of running processes, implemented as Linked List
{
    SimulProcess oldestProcess;
    SimulProcess newestProcess;
    int processTotal = 0;
    
    public ProcessList()
    {
        oldestProcess = null;
    }
    
    public void addProcess(SimulProcess toBeAdded)
    {
        if (oldestProcess == null)
        {
            oldestProcess = toBeAdded;
            newestProcess = toBeAdded;
            
            //System.out.println("Senpai notice me!"+(processTotal+1)); //debugging code
        }
        else
        {
            newestProcess.nextProcess = toBeAdded;
            newestProcess = toBeAdded;
            toBeAdded.nextProcess = null;
            //System.out.println("Senpai notice me again!"+(processTotal+1)); //debugging code
        }
        processTotal++;
    }
    
    public SimulProcess getProcess(String desc)
    {
        SimulProcess searchedProcess = oldestProcess;
        SimulProcess toBeReturned = null;
        while (searchedProcess != null)
        {
            if (searchedProcess.getDesc().equals(desc))
            {
                toBeReturned = searchedProcess;
            }
            searchedProcess = searchedProcess.nextProcess;
        }
        return toBeReturned;
    }
}
