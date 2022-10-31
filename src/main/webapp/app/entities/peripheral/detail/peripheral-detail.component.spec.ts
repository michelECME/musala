import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PeripheralDetailComponent } from './peripheral-detail.component';

describe('Peripheral Management Detail Component', () => {
  let comp: PeripheralDetailComponent;
  let fixture: ComponentFixture<PeripheralDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PeripheralDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ peripheral: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PeripheralDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PeripheralDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load peripheral on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.peripheral).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
