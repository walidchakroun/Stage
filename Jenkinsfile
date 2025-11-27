pipeline {
    agent any

    stages {



        stage('Maven Clean & Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('JUnit & Mockito Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Coverage & Report (JaCoCo)') {
            steps {
                jacoco(
                    execPattern: 'target/jacoco.exec',
                    classPattern: 'target/classes',
                    sourcePattern: 'src/main/java',
                    inclusionPattern: '**/*.class',
                    exclusionPattern: '**/*Test*'
                )
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.token=$SONAR_TOKEN \
                            -Dsonar.host.url=http://192.168.10.10:9000
                    '''
                }
            }
        }

        stage('Nexus Deploy') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t walidchakroun/stage .'
                sh 'docker image prune -f'
            }
        }

//        stage('Pre-Pull Docker Images') {
//            steps {
//                echo 'Ensuring required docker images are present locally...'
//                // Use 'docker pull' to cache the images
//                sh 'docker pull aquasec/trivy:latest'
//                sh 'docker pull realguess/jq:latest'
//                echo 'Images are pulled and ready for use.'
//            }
//        }

        stage('Dependency Scan (SCA - Trivy via Docker)') {
            steps {
                script {
                    // 1. Setup Cache Directory
                    sh 'mkdir -p $WORKSPACE/.trivy-cache'

                    // 2. Run Trivy SCA Scan - FIX APPLIED HERE
                    sh '''
                docker run --rm \
                    -v $WORKSPACE:/app \
                    -v $WORKSPACE/.trivy-cache:/root/.cache/ \
                    -w /app \
                    aquasec/trivy:latest \
                    fs --severity HIGH,CRITICAL \
                    --format json \
                    --output trivy-sca-report.json . || true
            '''

                    // 3. Display formatted vulnerability report
                    sh '''
                echo "--- TRIVY VULNERABILITY REPORT (HIGH/CRITICAL) ---"
                docker run --rm \
                    -v $WORKSPACE:/app \
                    -w /app \
                    realguess/jq:latest \
                    jq -r '
                        .Results[] | select(.Vulnerabilities) | {
                            Target: .Target,
                            Vulnerabilities: [
                                .Vulnerabilities[]
                                | select(.Severity == "CRITICAL" or .Severity == "HIGH")
                                | {
                                    Severity: .Severity,
                                    VulnerabilityID: .VulnerabilityID,
                                    PkgName: .PkgName,
                                    InstalledVersion: .InstalledVersion
                                }
                            ]
                        }
                    ' trivy-sca-report.json
            '''

                    echo '✅ Trivy SCA scan completed. Check the results above.'
                }
            }
        }

        stage('Trivy Scan - Git Repository') {
            steps {
                echo "🔍 Scanning Git repository for vulnerabilities, secrets, and licenses..."

                // Run Trivy scan and save JSON report
                sh '''
                    trivy repo . \
                        --format json \
                        --output trivy_repo_report.json \
                        --scanners vuln,secret,license \
                        --severity HIGH,CRITICAL
                '''

//                 Optional parsing logic (commented out)
//                 script {
//                     def report = readJSON file: '/var/lib/jenkins/workspace/Test/trivy_repo_report.json'
//                     def criticalVulns = []
//
//                     if (report instanceof Map && report.containsKey('Results')) {
//                         report = report.Results
//                     }
//
//                     if (report instanceof List) {
//                         for (item in report) {
//                             if (item.containsKey('Vulnerabilities') && item.Vulnerabilities) {
//                                 criticalVulns.addAll(
//                                     item.Vulnerabilities.findAll { v ->
//                                         v.Severity in ['HIGH', 'CRITICAL']
//                                     }
//                                 )
//                             }
//                         }
//                     }
//
//                     if (criticalVulns.size() > 0) {
//                         echo "❌ Critical or high vulnerabilities found: ${criticalVulns.size()}"
//                         error("Halting pipeline due to critical vulnerabilities.")
//                     } else {
//                         echo "✅ No critical vulnerabilities found in repository scan."
//                     }
//                 }
            }
        }

        stage('Trivy Scan - Docker Image') {
            steps {
                echo "🔍 Scanning Docker image for HIGH/CRITICAL vulnerabilities and secrets..."
                sh '''
                    trivy image walidchakroun/stage \
                        --format json \
                        --output trivy_docker_report.json \
                        --scanners vuln,secret,license \
                        --severity HIGH,CRITICAL
                '''
            }
        }

        stage('Gitleaks Scan - Secrets') {
            steps {
                echo "🕵️ Scanning repository for secrets using Gitleaks..."
                sh 'gitleaks detect --source . --report-format json --report-path gitleaks_report.json --exit-code 1 || true'
            }
        }

        // stage('Docker Hub Push') {
        //     steps {
        //         withCredentials([string(credentialsId: 'dockerhub', variable: 'DOCKERHUB_TOKEN')]) {
        //             sh 'echo $DOCKERHUB_TOKEN | docker login -u walidchakroun --password-stdin'
        //             sh 'docker push walidchakroun/stage'
        //         }
        //     }
        // }

        stage('Docker Compose') {
            steps {
                sh 'docker-compose -f docker-compose.yml up -d'
            }
        }

        // stage('OWASP ZAP Scan') {
        //     steps {
        //         echo "Running OWASP ZAP scan on the running Spring Boot container..."
        //         sh 'mkdir -p zap-reports'
        //         sh '''
        //             docker run --rm --network container:stage \
        //                 -v $PWD/zap-reports:/zap/wrk/:rw \
        //                 zaproxy/zap-stable zap-baseline.py \
        //                 -t http://localhost:8089/cours-classroom \
        //                 -r zap_report.html \
        //                 -J zap_report.json \
        //                 -d
        //         '''
        //         script {
        //             def zapReport = readJSON file: 'zap-reports/zap_report.json'
        //             def highAlerts = zapReport.site.collectMany { it.alerts ?: [] }
        //                                        .findAll { it.risk == 'High' || it.risk == 'Critical' }
        //             if (highAlerts) {
        //                 echo "High/Critical ZAP alerts found: ${highAlerts.size()}"
        //                 error("Halting pipeline due to high/critical security issues from ZAP!")
        //             } else {
        //                 echo "No high/critical alerts found by ZAP."
        //             }
        //         }
        //     }
        // }

    }

// --- Add the Post-Build Notification Section ---
    post {
        always {
            echo "📦 Archiving Trivy results..."
            archiveArtifacts artifacts: 'trivy_repo_report.json', allowEmptyArchive: true

            // If you plan to publish HTML or JSON visually later
            publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'trivy_repo_report.json',
                    reportName: 'Trivy FS Report'
            ])
        }

        success {
            script {
                // Extract HIGH + CRITICAL counts from Trivy JSON
                def trivyHigh = sh(
                        script: "grep -c '\"Severity\": \"HIGH\"' trivy_repo_report.json || true",
                        returnStdout: true
                ).trim()

                def trivyCrit = sh(
                        script: "grep -c '\"Severity\": \"CRITICAL\"' trivy_repo_report.json || true",
                        returnStdout: true
                ).trim()

                emailext(
                        subject: "✅ Pipeline SUCCESS — ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                        to: "walid.chakroun21@gmail.com",
                        mimeType: "text/html",

                        attachmentsPattern: "trivy_repo_report.json",

                        body: """
                <html>
                <body style="font-family:Segoe UI, sans-serif; background:#f6f6f6; padding:20px;">
                    <div style="max-width:700px; margin:auto; background:white; padding:25px; 
                                border-radius:10px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">

                        <h2 style="color:#2e7d32;">✅ Pipeline Successful</h2>
                        <p>
                            <b>Project:</b> ${env.JOB_NAME}<br>
                            <b>Build #:</b> ${env.BUILD_NUMBER}<br>
                            <b>Time:</b> ${new Date().format("yyyy-MM-dd HH:mm:ss",
                                TimeZone.getTimeZone('Europe/Paris'))}<br>
                        </p>

                        <hr>

                        <h3>📊 Trivy Security Summary</h3>
                        <table style="width:100%; border-collapse:collapse;">
                            <tr><th align="left">Severity</th><th align="center">Count</th></tr>
                            <tr><td>HIGH</td><td align="center">${trivyHigh}</td></tr>
                            <tr><td>CRITICAL</td><td align="center">${trivyCrit}</td></tr>
                        </table>

                        <hr>

                        <h4>📁 Reports</h4>
                        <p>
                            <a href="${env.BUILD_URL}artifact/trivy_repo_report.json" 
                               style="color:#1a73e8;">Download Trivy Report</a>
                        </p>

                        <hr>

                        <p style="text-align:center; color:#666; font-size:12px;">
                            🔒 Generated automatically by Jenkins DevSecOps Pipeline.
                        </p>
                    </div>
                </body>
                </html>
                """
                )
            }
        }

        failure {
            emailext(
                    subject: "❌ Pipeline FAILED — ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                    to: "walid.chakroun21@gmail.com",
                    mimeType: "text/html",

                    attachmentsPattern: "trivy_repo_report.json",

                    body: """
            <html>
            <body style="font-family:Segoe UI, sans-serif; background:#fff0f0; padding:20px;">
                <div style="max-width:700px; margin:auto; background:white; padding:25px; 
                            border-radius:10px; box-shadow:0 2px 8px rgba(255,0,0,0.15);">

                    <h2 style="color:#c62828;">❌ Pipeline Failed</h2>
                    <p>
                        <b>Project:</b> ${env.JOB_NAME}<br>
                        <b>Build #:</b> ${env.BUILD_NUMBER}<br>
                        <b>Time:</b> ${new Date().format("yyyy-MM-dd HH:mm:ss",
                            TimeZone.getTimeZone('Europe/Paris'))}<br>
                    </p>

                    <hr>

                    <p>
                        ⚠️ The pipeline has failed.<br>
                        Check the <a href="${env.BUILD_URL}console" 
                        style="color:#d32f2f;">console logs</a> for details.
                    </p>

                    <hr>

                    <p style="font-size:12px; color:#666; text-align:center;">
                        📎 Trivy report attached for debugging. Stay secure!
                    </p>
                </div>
            </body>
            </html>
            """
            )
        }
    }
}
