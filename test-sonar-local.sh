#!/bin/bash
# Test SonarCloud analysis locally before creating a PR

set -e  # Exit on any error

echo "=================================================="
echo "🔍 SonarCloud Local Analysis Test"
echo "=================================================="
echo ""

# Check if SONAR_TOKEN is set
if [ -z "$SONAR_TOKEN" ]; then
    echo "❌ ERROR: SONAR_TOKEN environment variable is not set!"
    echo ""
    echo "Please set it first:"
    echo "  export SONAR_TOKEN=your_token_here"
    echo ""
    echo "Get your token at: https://sonarcloud.io/account/security"
    exit 1
fi

# Check if sonar-scanner is installed
if ! command -v sonar-scanner &> /dev/null; then
    echo "❌ ERROR: sonar-scanner is not installed!"
    echo ""
    echo "Install it with:"
    echo "  brew install sonar-scanner"
    echo ""
    exit 1
fi

echo "✅ SONAR_TOKEN is set"
echo "✅ sonar-scanner is installed"
echo ""

# Step 1: Clean
echo "🧹 Step 1/5: Cleaning previous builds..."
./gradlew clean
echo ""

# Step 2: Build
echo "🔨 Step 2/5: Building debug APK..."
./gradlew assembleDebug --stacktrace
echo ""

# Step 3: Test
echo "🧪 Step 3/5: Running unit tests..."
./gradlew test --stacktrace
echo ""

# Step 4: Coverage
echo "📊 Step 4/5: Generating coverage report..."
./gradlew jacocoTestReport --stacktrace
echo ""

# Verify coverage report exists
if [ ! -f "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml" ]; then
    echo "❌ ERROR: Coverage report not found!"
    echo "Expected at: app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
    exit 1
fi

echo "✅ Coverage report generated successfully"
echo ""

# Step 5: SonarCloud Analysis
echo "🔍 Step 5/5: Running SonarCloud analysis..."
echo "This may take 1-2 minutes..."
echo ""
sonar-scanner

echo ""
echo "=================================================="
echo "✅ ALL CHECKS PASSED!"
echo "=================================================="
echo ""
echo "📈 View detailed results at:"
echo "https://sonarcloud.io/project/overview?id=Ololoking_android-cicd-testing-sample"
echo ""
echo "Next steps:"
echo "1. Review the SonarCloud dashboard"
echo "2. Fix any issues if needed"
echo "3. Commit and push your changes"
echo "4. Create a PR - CI will run the same checks!"
echo ""

