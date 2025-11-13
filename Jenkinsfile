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

        stage('Dependency Scan (SCA - Trivy via Docker)') {
            steps {
                script {
                    // 1. Run Trivy SCA Scan
                    sh '''
                        docker run --rm \
                            -v $WORKSPACE:/app \
                            -w /app \
                            aquasec/trivy:latest \
                            fs --severity HIGH,CRITICAL \
                            --format json \
                            --output trivy-sca-report.json . || true
                    '''

                    // 2. Display formatted vulnerability report
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

                // Optional parsing logic (commented out)
                 script {
                     def report = readJSON file: '/var/lib/jenkins/workspace/Test/trivy_repo_report.json'
                     def criticalVulns = []

                     if (report instanceof Map && report.containsKey('Results')) {
                         report = report.Results
                     }

                     if (report instanceof List) {
                         for (item in report) {
                             if (item.containsKey('Vulnerabilities') && item.Vulnerabilities) {
                                 criticalVulns.addAll(
                                     item.Vulnerabilities.findAll { v ->
                                         v.Severity in ['HIGH', 'CRITICAL']
                                     }
                                 )
                             }
                         }
                     }

                     if (criticalVulns.size() > 0) {
                         echo "❌ Critical or high vulnerabilities found: ${criticalVulns.size()}"
                         error("Halting pipeline due to critical vulnerabilities.")
                     } else {
                         echo "✅ No critical vulnerabilities found in repository scan."
                     }
                 }
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
            archiveArtifacts artifacts: 'trivy_repo_report.json', allowEmptyArchive: true
        }
        success {
            // This runs only if the pipeline finished with success.
            emailext(
                    subject: "✅ Pipeline SUCCESS: ${currentBuild.fullDisplayName}",
                    body: """Hello Team,
                    The pipeline **completed successfully**!
        
                    Build URL: ${env.BUILD_URL}
                    """,
                    to: "walid.chakroun21@gmail.com"
                    // NOTE: I recommend removing the attachmentsPattern from 'success'
                )
        }


        failure {
            // This runs only if the pipeline failed.
            emailext(
                    subject: "❌ Pipeline FAILED: ${currentBuild.fullDisplayName}",
                    body: """Hello Team,
                    The pipeline failed. Check the attached Trivy report for details.
                    Build URL: ${env.BUILD_URL}
                    """,
                    to: "walid.chakroun21@gmail.com",

                    // Attach the file using its name relative to the current workspace root.
                    // This is the most reliable way.
                    attachmentsPattern: 'trivy_repo_report.json'
                )
        }
        // You can also use 'unstable', 'fixed', 'aborted', etc.
    }
}
