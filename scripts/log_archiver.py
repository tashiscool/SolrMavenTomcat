import glob
import os
import time
import gzip

#CHANGEME: point this to the tomcat logs area.  It doesn't matter if it ends in a slash.
TOMCAT_LOG_ROOT="/export/home/apps/tomcat/logs/"

# CHANGEME: this is the big one - make sure that there is a "path" (meaning root path)
# and a file pattern that is used to match log files, for each set of logs that are to be
# managed.  Anything matching the given pattern, in the given path, will be archived.
MANAGED_LOGS = [ \
    {"path":TOMCAT_LOG_ROOT,
    "pattern":"*access*.[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9].log"},
    {"path":TOMCAT_LOG_ROOT,
    "pattern":"*manager*.[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9].log"},
    {"path":TOMCAT_LOG_ROOT,
    "pattern":"*catalina*.[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9].log"}]

# You shouldn't have to change this
ARCHIVED_EXTENSION = ".gz"

# this defines the age at which logs are zipped up
ARCHIVE_AGE_IN_DAYS = 2

SECONDS_PER_DAY = 60*60*24

def archive_file(filename):
    """Archives a file by gzipping it in the same directory; the original file is then removed"""
    print "archiving file: "+filename
    f_in = open(filename, 'rb')
    real_f = open(filename+ARCHIVED_EXTENSION, 'wb')
    # use GzipFile directly so that we separate the full file path from its name
    f_out = gzip.GzipFile(os.path.basename(filename+ARCHIVED_EXTENSION), fileobj = real_f)
    f_out.writelines(f_in)
    f_out.close()
    real_f.close()
    # now remove the original
    os.remove(filename)

def purge_file(filename):
    """Purges a file (=delete!)."""
    print "purging file: "+filename
    os.remove(filename)

def is_older(filename, days):
    """checks the age of the given file and determines if it is older than the given # of days"""
    age_in_days = (time.time() - os.stat(filename).st_mtime)/SECONDS_PER_DAY
    return age_in_days > days
            
def archive_logs(path, pattern):
    """searches for log files and decides whether to archive them, or leave them alone."""
    l = glob.glob(os.path.join(path,pattern))
    for f in l:
        if is_older(f,ARCHIVE_AGE_IN_DAYS):
            archive_file(f)
        else:
            print "leaving file:"+f

def purge_archives(path, pattern, days_to_keep):
    """Purges old gzipped logs that are older than days_to_keep"""
    # delete old zipped logs
    l = glob.glob(os.path.join(path,pattern+ARCHIVED_EXTENSION))
    for f in l:
        if is_older(f,days_to_keep):
            purge_file(f)
        else:
            print "leaving file:"+f

# this is the main loop - go through each "managed log" area and archive+purge
for log in MANAGED_LOGS:
    archive_logs(log["path"],log["pattern"])
    purge_archives(log["path"],log["pattern"],2)