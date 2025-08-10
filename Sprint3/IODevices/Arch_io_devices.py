### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('io_devicesArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_raspdevice', graph_attr=nodeattr):
          mind=Custom('mind','./qakicons/symActorWithobjSmall.png')
          sonardevice=Custom('sonardevice','./qakicons/symActorWithobjSmall.png')
          leddevice=Custom('leddevice','./qakicons/symActorWithobjSmall.png')
     sonardevice >> Edge( label='sonardata', **eventedgeattr, decorate='true', fontcolor='red') >> mind
     mind >> Edge( label='containerhere', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     mind >> Edge( label='stopActions', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     mind >> Edge( label='resumeActions', **eventedgeattr, decorate='true', fontcolor='red') >> sys
diag
