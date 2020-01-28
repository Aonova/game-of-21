package source;

public class ProcessStack //Arbitrary orginization class for starting Simultaneous Processes, implemented as Linked List Stack/==========================================
{
    private SimulProcess topProcess;
    
    public ProcessStack()
    {
        topProcess = null;
    }
    
    public void pushProcess(SimulProcess process)
    {
        if (topProcess == null) //empty stack
        {
            topProcess = process;
            process.nextProcess = null;
        }
        else
        {
            process.nextProcess = topProcess;
            topProcess = process;
        }
        
    }
    
    public SimulProcess popProcess()
    {
        if (topProcess!= null)
        {
            SimulProcess toBePopped = topProcess;
            topProcess = topProcess.nextProcess;
            return toBePopped;
        }
        else
        {
            return null;
        }
    }
    
    public boolean isEmpty()
    {
        if (topProcess == null)
        {
            return true;
        }
        
        else
        {
            return false;
        }
    }
    
     public void runProcesses()
    {
        while (this.isEmpty() ==  false)
        {
            SimulProcess toRun = this.popProcess();
            toRun.start();
        }
    }
}//End ProcessStack Class/===============================================================================================================================