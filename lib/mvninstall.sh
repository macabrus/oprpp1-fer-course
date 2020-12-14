

mvn install:install-file \
    -DgroupId=hr.fer.zemris.java.fractals \
    -DartifactId=fractal-viewer \
    -Dversion=1.0 \
    -Dpackaging=jar \
    -DgeneratePom=true \
    -Dfile=fractal-viewer-1.0.jar \
    -Djavadoc=fractal-viewer-1.0-javadoc.jar