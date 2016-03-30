import groovy.json.JsonSlurper

String skip = properties.getAt('sonar.skip')
if (skip == 'true') {
    log.info('sonar.skip = true: Skipping Sonar issues file analysis.')
    return;
}
String issueFile = properties.getAt('sonar.issues.file')
filepath = new File(issueFile)
log.info('Sonar Issues file: ' + filepath)
JsonSlurper slurper = new JsonSlurper()
String contents = filepath.getText('UTF-8')
def json = slurper.parseText(contents)

def major = 0
def critical = 0
def blocker = 0
def report = []

json.issues.each {
    issue = it
    if (issue.isNew && issue.status == "OPEN") {
        switch (issue.severity) {
            case 'MAJOR':
                major++
                report << issue
                break
            case 'CRITICAL':
                critical++
                report << issue
                break
            case 'BLOCKER':
                blocker++
                report << issue
                break
        }
    }
}
red = major + critical + blocker
if (red > 0) {
    msg = 'There are ' + red + ' new issue(s) that have severity of MAJOR or above (BLOCKER or CRITICAL)!'
    log.error(msg)
    log.error('')
    if (blocker) {
        log.error('Blocker: +' + blocker)
    }
    if (critical) {
        log.error('Critical: +' + critical)
    }
    if (major) {
        log.error('Major: +' + major)
    }
    fail(msg)
} else {
    log.info('No new issues have been found')
}
