package com.wuligao.utils.system;

import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.*;

import java.io.*;
import java.util.*;

@Slf4j
public class MonitorCpuUtils {

    public static void main(String[] args) throws SigarException {
        Sigar sigar = new Sigar();
        //获取内存
        try {
            Mem mem = sigar.getMem();
            int memTotal = (int) Math.ceil((double) mem.getTotal() / (1024L * 1024L * 1024L));
            String memFree = String.format("%.1f", (double) mem.getFree() / (1024L * 1024L * 1024L));
            String memUser = String.format("%.0f", (memTotal - Double.parseDouble(memFree)) / memTotal * 100);
            System.out.println("内存总量:" + memTotal + "G");
            System.out.println("内存剩余量:" + memFree + "G");
            System.out.println("内存使用率:" + memUser + "%");
        } catch (SigarException e1) {
            e1.printStackTrace();
        }
        //获取硬盘
        Long ypTotal = 0L;
        Long ypfree = 0L;
        try {
            FileSystem[] fslist = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = null;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小
                        ypTotal += usage.getTotal();
                        ypfree += usage.getFree();
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
            }
            int hdTotal = (int) ((double) ypTotal / (1024L * 1024L));
            String hdfree = String.format("%.1f", (double) ypfree / (1024L * 1024L));
            String hdUser = String.format("%.0f", (hdTotal - Double.parseDouble(hdfree)) / hdTotal * 100);
            System.out.println("硬盘总量:" + hdTotal + "G");
            System.out.println("硬盘剩余量:" + hdfree + "G");
            System.out.println("硬盘使用率:" + hdUser + "%");
        } catch (SigarException e1) {
            e1.printStackTrace();
        }
        if (SystemVersionUtils.getOsName().equals("win")) {
            //获取CPU使用率(Windows)
            cpu();
        } else {
            //获取CPU使用率(Linux)
            String lcpu = cpuUsage() + "%";
            System.out.println("cpu使用率:" + lcpu);
        }

        System.out.println("系统资源占用" + serverDetails());
    }

    /**
     * cpu:(cpu信息). <br/>
     *
     * @throws SigarException
     * @author liuweiying
     * @since JDK 1.6
     */
    public static void cpu() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = null;
        cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            System.out.println("第" + (i + 1) + "块CPU信息");
            System.out.println("CPU用户使用率:    " + CpuPerc.format(cpuList[i].getUser()));// 用户使用率
            System.out.println("CPU系统使用率:    " + CpuPerc.format(cpuList[i].getSys()));// 系统使用率
            System.out.println("CPU当前等待率:    " + CpuPerc.format(cpuList[i].getWait()));// 当前等待率
            System.out.println("CPU当前错误率:    " + CpuPerc.format(cpuList[i].getNice()));//
            System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpuList[i].getIdle()));// 当前空闲率
            System.out.println("CPU总的使用率:    " + CpuPerc.format(cpuList[i].getCombined()));// 总的使用率
        }
    }


    public static Map<String, Object> serverDetails() {
        Map<String, Object> map = new HashMap<>(10);
        Sigar sigar = new Sigar();
        //获取内存
        try {
            Mem mem = sigar.getMem();
            int memTotal = (int) Math.ceil((double) mem.getTotal() / (1024L * 1024L * 1024L));
            String memFree = String.format("%.1f", (double) mem.getFree() / (1024L * 1024L * 1024L));
            String memUser = String.format("%.0f", (memTotal - Double.parseDouble(memFree)) / memTotal * 100);
//            System.out.println("内存总量:" + memTotal + "G");
            map.put("memoryAll", memTotal);
            map.put("memoryResidue", Double.parseDouble(memFree));
//            System.out.println("内存使用率:" + memUser + "%");
            map.put("memoryPercent", Double.parseDouble(memUser));
        } catch (SigarException e1) {
            e1.printStackTrace();
        }
        //获取硬盘
        Long ypTotal = 0L;
        Long ypfree = 0L;
        try {
            FileSystem[] fslist = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = null;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0:
                        // TYPE_UNKNOWN ：未知
                        break;
                    case 1:
                        // TYPE_NONE
                        break;
                    case 2:
                        // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小
                        ypTotal += usage.getTotal();
                        ypfree += usage.getFree();
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
            }
            int hdTotal = (int) ((double) ypTotal / (1024L * 1024L));
            String hdfree = String.format("%.1f", (double) ypfree / (1024L * 1024L));
            String hdUser = String.format("%.0f", (hdTotal - Double.parseDouble(hdfree)) / hdTotal * 100);
//            System.out.println("硬盘总量:"+hdTotal+"G");
            map.put("diskAll", hdTotal);
//            System.out.println("硬盘剩余量:"+hdfree+"G");
            map.put("diskResidue", Double.parseDouble(hdfree));
//            System.out.println("硬盘使用率:" + hdUser + "%");
            map.put("diskPercent", Double.parseDouble(hdUser));


        } catch (SigarException e1) {
            e1.printStackTrace();
        }
        //获取CPU使用率(Windows)
        if (SystemVersionUtils.getOsName().equals("win")) {
            // 获取暂不可用先返回固定值
//            String wcpu = String.format("%.0f",getCpuRatioForWindows())+"%";
//            System.out.println("Windows的CPU使用率:"+wcpu);
            map.put("cpuPercent", 56.0);
        } else {
            //获取CPU使用率(Linux)
            String lcpu = cpuUsage() + "%";
            map.put("cpuPercent", cpuUsage());
        }
        return map;
    }


    //获取windows系统cpu使用率
    private static double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "//system32//wbem//WMIC.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(500);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(100 * (busytime) / (busytime + idletime)).doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    private static long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream(), "gbk");
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < 10) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = Bytes.substring(line, capidx, cmdidx - 1).trim();
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    idletime += Long.valueOf(Bytes.substring(line, kmtidx, rocidx - 1).trim()).longValue();
                    idletime += Long.valueOf(Bytes.substring(line, umtidx, wocidx - 1).trim()).longValue();
                    continue;
                }
                kneltime += Long.valueOf(Bytes.substring(line, kmtidx, rocidx - 1).trim()).longValue();
                usertime += Long.valueOf(Bytes.substring(line, umtidx, wocidx - 1).trim()).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class Bytes {
        /** */
        /**
         * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在
         * 包含汉字的字符串时存在隐患，现调整如下：
         *
         * @param src       要截取的字符串
         * @param start_idx 开始坐标（包括该坐标)
         * @param end_idx   截止坐标（包括该坐标）
         * @return
         * @throws UnsupportedEncodingException
         */
        public static String substring(String src, int start_idx, int end_idx) throws UnsupportedEncodingException {
            byte[] b = src.getBytes();
            String tgt = "";
            for (int i = start_idx; i <= end_idx; i++) {
                tgt += (char) b[i];
            }
            return tgt;
        }
    }

    /**
     * 功能：获取Linux系统cpu使用率
     */
    public static int cpuUsage() {
        try {
            Map<?, ?> map1 = cpuinfo();
            Thread.sleep(500);
            Map<?, ?> map2 = cpuinfo();
            if (map1.size() > 0 && map2.size() > 0) {
                long user1 = Long.parseLong(map1.get("user").toString());
                long nice1 = Long.parseLong(map1.get("nice").toString());
                long system1 = Long.parseLong(map1.get("system").toString());
                long idle1 = Long.parseLong(map1.get("idle").toString());

                long user2 = Long.parseLong(map2.get("user").toString());
                long nice2 = Long.parseLong(map2.get("nice").toString());
                long system2 = Long.parseLong(map2.get("system").toString());
                long idle2 = Long.parseLong(map2.get("idle").toString());

                long total1 = user1 + system1 + nice1;
                long total2 = user2 + system2 + nice2;
                float total = total2 - total1;

                long totalIdle1 = user1 + nice1 + system1 + idle1;
                long totalIdle2 = user2 + nice2 + system2 + idle2;
                float totalidle = totalIdle2 - totalIdle1;

                float cpusage = (total / totalidle) * 100;
                return (int) cpusage;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 功能：CPU使用信息
     */
    public static Map<?, ?> cpuinfo() {
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/stat"));
            buffer = new BufferedReader(inputs);
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("cpu")) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    List<String> temp = new ArrayList<String>();
                    while (tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        temp.add(value);
                    }
                    map.put("user", temp.get(1));
                    map.put("nice", temp.get(2));
                    map.put("system", temp.get(3));
                    map.put("idle", temp.get(4));
                    map.put("iowait", temp.get(5));
                    map.put("irq", temp.get(6));
                    map.put("softirq", temp.get(7));
                    map.put("stealstolen", temp.get(8));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null && inputs != null) {
                    buffer.close();
                    inputs.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return map;
    }

}
