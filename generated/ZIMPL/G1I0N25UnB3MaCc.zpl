
set Bins := { 1 .. 29 };
#Bins set

set Items := { 1 .. 25 };
#Items set

set Assigment := Bins * Items;


param capacity[Bins]:= <1> 50.0,
                       <2> 50.0,
                       <3> 50.0,
                       <4> 50.0,
                       <5> 50.0,
                       <6> 100.0,
                       <7> 100.0,
                       <8> 100.0,
                       <9> 100.0,
                       <10> 100.0,
                       <11> 100.0,
                       <12> 100.0,
                       <13> 100.0,
                       <14> 100.0,
                       <15> 100.0,
                       <16> 100.0,
                       <17> 100.0,
                       <18> 100.0,
                       <19> 150.0,
                       <20> 150.0,
                       <21> 150.0,
                       <22> 150.0,
                       <23> 150.0,
                       <24> 150.0,
                       <25> 150.0,
                       <26> 150.0,
                       <27> 150.0,
                       <28> 150.0,
                       <29> 150.0;

param cost[Bins]:= <1> 70.71067811865476,
                   <2> 70.71067811865476,
                   <3> 70.71067811865476,
                   <4> 70.71067811865476,
                   <5> 70.71067811865476,
                   <6> 100.0,
                   <7> 100.0,
                   <8> 100.0,
                   <9> 100.0,
                   <10> 100.0,
                   <11> 100.0,
                   <12> 100.0,
                   <13> 100.0,
                   <14> 100.0,
                   <15> 100.0,
                   <16> 100.0,
                   <17> 100.0,
                   <18> 100.0,
                   <19> 122.4744871391589,
                   <20> 122.4744871391589,
                   <21> 122.4744871391589,
                   <22> 122.4744871391589,
                   <23> 122.4744871391589,
                   <24> 122.4744871391589,
                   <25> 122.4744871391589,
                   <26> 122.4744871391589,
                   <27> 122.4744871391589,
                   <28> 122.4744871391589,
                   <29> 122.4744871391589;

param weigth[Items]:= <1> 33.50328720323222,
                      <2> 102.92904182763124,
                      <3> 66.30178098945615,
                      <4> 56.75827749785896,
                      <5> 54.92092377416343,
                      <6> 31.075090424509227,
                      <7> 59.61963199290508,
                      <8> 61.17923873318021,
                      <9> 95.77346984689235,
                      <10> 42.71536910319569,
                      <11> 23.41716227835829,
                      <12> 21.393930545084096,
                      <13> 106.17635468172082,
                      <14> 57.60612760234912,
                      <15> 87.03540293348408,
                      <16> 102.52680479840538,
                      <17> 78.63275900640204,
                      <18> 48.658881103135926,
                      <19> 59.21375891040026,
                      <20> 80.82675525556337,
                      <21> 93.18924445743245,
                      <22> 80.7705084784468,
                      <23> 37.32110195411285,
                      <24> 96.25914670370345,
                      <25> 28.773623890624172;

var y[Bins] binary;
#Is the bin used?

var x[Assigment] binary; 
#Is the item packed in bin?

minimize cost: sum <i> in Bins : cost[i]*y[i];
#Minimize total cost of used bins

#Each item j is packed in exactly one bin
subto assign :
       forall <j> in Items do
              sum <i> in Bins : x[i,j] == 1;

#Every bin should pack no more items than its capacity if it's used
subto limit :
       forall <i> in Bins do 
              sum <j> in Items : weigth[j] * x[i,j]  <= capacity[i] * y[i];

