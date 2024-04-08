"use client";

import TripCard from "@/components/TripCard";
import { getReservation } from "@/service/reservationService";
import { getTrip } from "@/service/tripService";
import { Currency } from "@/types/currency";
import { Reservation } from "@/types/reservation";
import { Trip } from "@/types/trip";
import { Button } from "@nextui-org/react";
import { useQuery } from "@tanstack/react-query";
import { useCookies } from "next-client-cookies";
import { useRouter } from "next/navigation";

export default function Success() {
  const cookies = useCookies();
  const router = useRouter();
  const tripId = parseInt(cookies.get("trip") || "0");
  const reservationId = parseInt(cookies.get("reservation") || "0");
  const currency: Currency = (cookies.get("currency") as Currency) || "EUR";

  const trip = useQuery<Trip>({
    queryKey: ["trip", tripId, currency],
    queryFn: () => getTrip(tripId, { currency }),
  }).data;

  const reservation = useQuery<Reservation>({
    queryKey: ["reservation", reservationId],
    queryFn: () => getReservation(reservationId),
  }).data;

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Your reservation has been created successfully.
        </h1>
      </div>
      <div className="flex flex-col px-4 lg:w-[512px] gap-8 lg:self-center">
        <p className="text-center">
          Your reservation ID is {reservation?.id}, with {reservation?.seats}{" "}
          seat{reservation && reservation.seats > 1 && "s"} reserved.
        </p>
        {trip && <TripCard trip={trip} clickable={false} />}
        <Button
          color="primary"
          className="self-center"
          onClick={() => {
            cookies.remove("trip");
            cookies.remove("seats");
            cookies.remove("reservation");
            router.push("/");
          }}
        >
          Go home
        </Button>
      </div>
    </div>
  );
}
