package com.freewave.domain.resume.enums;

import com.freewave.domain.common.exception.InvalidRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TechStack {
    // 프로그래밍 언어
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    TYPESCRIPT("TypeScript"),
    KOTLIN("Kotlin"),
    SWIFT("Swift"),
    GO("Go"),
    RUST("Rust"),
    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    PHP("PHP"),
    RUBY("Ruby"),
    SCALA("Scala"),
    DART("Dart"),
    HASKELL("Haskell"),
    PERL("Perl"),
    R("R"),
    GROOVY("Groovy"),
    CLOJURE("Clojure"),

    // 프론트엔드 프레임워크 및 라이브러리
    REACT("React.js"),
    ANGULAR("Angular"),
    VUE("Vue.js"),
    NEXT("Next.js"),
    SVELTE("Svelte"),
    JQUERY("jQuery"),
    BACKBONE("Backbone.js"),
    EMBER("Ember.js"),
    PREACT("Preact"),
    SOLID_JS("SolidJS"),
    GATSBY("Gatsby.js"),
    NUXT("Nuxt.js"),
    REMIX("Remix"),
    ASTRO("Astro"),

    // 백엔드 프레임워크 및 라이브러리
    SPRING("Spring Framework"),
    SPRING_BOOT("Spring Boot"),
    DJANGO("Django"),
    FLASK("Flask"),
    EXPRESS("Express.js"),
    LARAVEL("Laravel"),
    DOTNET(".NET"),
    RUBY_ON_RAILS("Ruby on Rails"),
    NESTJS("NestJS"),
    FASTAPI("FastAPI"),
    PHOENIX("Phoenix"),
    SYMFONY("Symfony"),
    ASPNET("ASP.NET"),
    STRAPI("Strapi"),

    // CSS 프레임워크 및 도구
    TAILWIND("Tailwind CSS"),
    BOOTSTRAP("Bootstrap"),
    SASS("Sass"),
    LESS("Less"),
    STYLED_COMPONENTS("Styled Components"),
    EMOTION("Emotion"),
    BULMA("Bulma"),
    FOUNDATION("Foundation"),
    MATERIALIZE("Materialize CSS"),
    CHAKRA_UI("Chakra UI"),
    MATERIAL_UI("Material UI"),
    ANT_DESIGN("Ant Design"),
    SEMANTIC_UI("Semantic UI"),

    // UI/UX 디자인 도구
    FIGMA("Figma"),
    SKETCH("Sketch"),
    ADOBE_XD("Adobe XD"),
    INVISION("InVision"),
    ZEPLIN("Zeplin"),
    ADOBE_ILLUSTRATOR("Adobe Illustrator"),
    ADOBE_PHOTOSHOP("Adobe Photoshop"),
    PROTOPIE("ProtoPie"),
    AXURE("Axure RP"),
    FRAMER("Framer"),
    BALSAMIQ("Balsamiq"),
    PRINCIPLE("Principle"),
    ADOBE_AFTER_EFFECTS("Adobe After Effects"),
    UIZARD("Uizard"),
    KHROMA("Khroma"),
    ATTENTION_INSIGHT("Attention Insight"),
    ADOBE_FIREFLY("Adobe Firefly"),
    VISILY("Visily"),

    // 데이터베이스
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    ORACLE("Oracle"),
    MONGODB("MongoDB"),
    REDIS("Redis"),
    ELASTICSEARCH("Elasticsearch"),
    MARIADB("MariaDB"),
    MSSQL("Microsoft SQL Server"),
    DYNAMODB("Amazon DynamoDB"),
    CASSANDRA("Apache Cassandra"),
    COUCHDB("CouchDB"),
    NEO4J("Neo4j"),
    FIREBASE_FIRESTORE("Firebase Firestore"),
    SQLITE("SQLite"),
    INFLUXDB("InfluxDB"),

    // 인프라 및 DevOps
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    JENKINS("Jenkins"),
    GITHUB_ACTIONS("GitHub Actions"),
    TERRAFORM("Terraform"),
    ANSIBLE("Ansible"),
    GITLAB_CI("GitLab CI/CD"),
    CIRCLE_CI("Circle CI"),
    TRAVIS_CI("Travis CI"),
    PROMETHEUS("Prometheus"),
    GRAFANA("Grafana"),
    NGINX("NGINX"),
    APACHE("Apache HTTP Server"),
    PUPPET("Puppet"),
    CHEF("Chef"),

    // 클라우드
    AWS("Amazon Web Services"),
    AZURE("Microsoft Azure"),
    GCP("Google Cloud Platform"),
    HEROKU("Heroku"),
    DIGITAL_OCEAN("Digital Ocean"),
    IBM_CLOUD("IBM Cloud"),
    ALIBABA_CLOUD("Alibaba Cloud"),
    ORACLE_CLOUD("Oracle Cloud"),
    VERCEL("Vercel"),
    NETLIFY("Netlify"),
    CLOUDFLARE("Cloudflare"),
    LINODE("Linode"),

    // 모바일
    ANDROID("Android"),
    IOS("iOS"),
    REACT_NATIVE("React Native"),
    FLUTTER("Flutter"),
    XAMARIN("Xamarin"),
    IONIC("Ionic"),
    CORDOVA("Apache Cordova"),
    KOTLIN_MULTIPLATFORM("Kotlin Multiplatform"),
    SWIFT_UI("SwiftUI"),
    JETPACK_COMPOSE("Jetpack Compose"),

    // 테스트 도구
    JEST("Jest"),
    MOCHA("Mocha"),
    CYPRESS("Cypress"),
    SELENIUM("Selenium"),
    JUNIT("JUnit"),
    TESTNG("TestNG"),
    PUPPETEER("Puppeteer"),
    JASMINE("Jasmine"),
    ENZYME("Enzyme"),
    TESTING_LIBRARY("Testing Library"),
    PLAYWRIGHT("Playwright"),

    // 상태 관리
    REDUX("Redux"),
    MOBX("MobX"),
    RECOIL("Recoil"),
    ZUSTAND("Zustand"),
    JOTAI("Jotai"),
    VUEX("Vuex"),
    PINIA("Pinia"),
    CONTEXT_API("React Context API"),
    NGXS("NGXS"),

    // 웹 개발 도구
    WEBPACK("Webpack"),
    VITE("Vite"),
    PARCEL("Parcel"),
    ROLLUP("Rollup"),
    BABEL("Babel"),
    ESLINT("ESLint"),
    PRETTIER("Prettier"),
    STORYBOOK("Storybook"),
    CHROME_DEVTOOLS("Chrome DevTools"),
    POSTMAN("Postman"),
    VS_CODE("Visual Studio Code"),
    WEBSTORM("WebStorm"),

    // 3D 및 그래픽
    THREE_JS("Three.js"),
    WEBGL("WebGL"),
    UNITY("Unity"),
    UNREAL_ENGINE("Unreal Engine"),
    BLENDER("Blender"),
    D3("D3.js"),
    CANVAS_API("Canvas API"),
    SVG("SVG"),
    BABYLON_JS("Babylon.js"),
    PIXIJS("PixiJS"),

    // AR/VR
    ARKIT("ARKit"),
    ARCORE("ARCore"),
    VUFORIA("Vuforia"),
    OCULUS_SDK("Oculus SDK"),
    AFRAME("A-Frame"),
    WEBXR("WebXR"),

    // 기타
    GIT("Git"),
    GRAPHQL("GraphQL"),
    KAFKA("Apache Kafka"),
    HADOOP("Apache Hadoop"),
    SPARK("Apache Spark"),
    WEBSOCKET("WebSocket"),
    REST_API("REST API"),
    GRPC("gRPC"),
    OAUTH("OAuth"),
    JWT("JSON Web Token"),
    MICRO_FRONTENDS("Micro Frontends"),
    HEADLESS_CMS("Headless CMS"),
    PWA("Progressive Web App"),
    WEB_COMPONENTS("Web Components"),
    WEBASSEMBLY("WebAssembly"),
    SERVERLESS("Serverless"),
    JAMSTACK("JAMstack"),
    OPENAI_API("OpenAI API"),
    WEB3("Web3.js"),
    BLOCKCHAIN("Blockchain");

    private final String displayName;

    public static TechStack of(String techStack) {
        return Arrays.stream(TechStack.values())
                .filter(ts -> ts.name().equalsIgnoreCase(techStack))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Invalid TechStack: " + techStack));
    }
}
