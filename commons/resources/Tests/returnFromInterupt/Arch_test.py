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
with Diagram('testArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_testing', graph_attr=nodeattr):
          test1=Custom('test1','./qakicons/symActorWithobjSmall.png')
          test2interrupt=Custom('test2interrupt','./qakicons/symActorWithobjSmall.png')
          test_reply_after_interrupt=Custom('test_reply_after_interrupt','./qakicons/symActorWithobjSmall.png')
     test1 >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> test2interrupt
     test1 >> Edge( label='nooalarm', **eventedgeattr, decorate='true', fontcolor='red') >> test2interrupt
     test2interrupt >> Edge(color='magenta', style='solid', decorate='true', label='<testrequest<font color="darkgreen"> ok</font> &nbsp; >',  fontcolor='magenta') >> test_reply_after_interrupt
diag
